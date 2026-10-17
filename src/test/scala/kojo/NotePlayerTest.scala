package kojo

import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.scalajs.js

/**
 * `playNote` must QUEUE the notes as on the desktop. The RealtimeNotePlayer of the desktop adds the note to
 * the MIDI track at a cursor and moves the cursor forward by the duration; three consecutive calls are a
 * melody of three notes. Without the cursor every note would start at `currentTime`: the three would play
 * at the same moment, like a chord.
 *
 * Two layers:
 *  - a fake context that records the start/stop moments and the waveform -- the rules of the cursor (idle:
 *    at once, playing: at the end of the previous one, the instrument only for the next ones) are tested
 *    with exact numbers;
 *  - a real OfflineAudioContext: the same player really plays, and WHICH note is heard in each time window
 *    is measured with Goertzel. This catches what the fake context cannot see (for example the envelope
 *    silencing a note too early).
 */
class NotePlayerTest extends AsyncFunSuite with Matchers {
  implicit override def executionContext: scala.concurrent.ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  /** A fake AudioContext, with a clock that is set by hand, that records start/stop/type/frequency. */
  class FakeContext {
    val oscillators = mutable.ArrayBuffer.empty[js.Dynamic]
    var silenced = 0
    private def node(): js.Dynamic = js.Dynamic.literal(
      connect = (_: js.Any) => (),
      disconnect = () => { silenced += 1 }
    )
    var resumed = 0
    val ctx: js.Dynamic = js.Dynamic.literal(
      currentTime = 0.0,
      destination = node()
    )
    ctx.resume = () => { resumed += 1 }
    def suspend(): Unit = ctx.state = "suspended"
    ctx.createOscillator = () => {
      val o = node()
      o.frequency = js.Dynamic.literal(value = 0.0)
      o.start = (t: Double) => { o.startAt = t }
      o.stop = (t: Double) => { o.stopAt = t }
      o.connect = (target: js.Dynamic) => { o.gainNode = target }
      oscillators += o
      o
    }
    ctx.createGain = () => {
      val g = node()
      g.gain = js.Dynamic.literal(
        setValueAtTime = (level: Double, _: Double) => { g.level = level },
        exponentialRampToValueAtTime = (_: Double, _: Double) => ()
      )
      g
    }
    def clock(t: Double): Unit = ctx.currentTime = t
    def starts: Seq[Double] = oscillators.map(_.startAt.asInstanceOf[Double]).toSeq
    def stops: Seq[Double] = oscillators.map(_.stopAt.asInstanceOf[Double]).toSeq
    def types: Seq[String] = oscillators.map(_.`type`.asInstanceOf[String]).toSeq
    def frequencies: Seq[Double] = oscillators.map(_.frequency.value.asInstanceOf[Double]).toSeq
    /** The initial level of the gain node that each oscillator is connected to. */
    def levels: Seq[Double] = oscillators.map(_.gainNode.level.asInstanceOf[Double]).toSeq
  }

  private def player(): (NotePlayer, FakeContext) = {
    val c = new FakeContext
    val p = new NotePlayer
    p.audioContext = c.ctx
    (p, c)
  }

  private def builtinsWithFake(): (kojo.syntax.Builtins, FakeContext) = {
    implicit val kojoWorld: KojoWorld = new TestKojoWorld()
    val b = new kojo.syntax.Builtins()
    val c = new FakeContext
    b.notePlayer.audioContext = c.ctx
    (b, c)
  }

  private def close(a: Seq[Double], b: Seq[Double]) = {
    a.size shouldBe b.size
    a.zip(b).foreach { case (x, y) => x shouldBe y +- 1e-9 }
    succeed
  }

  test("consecutive notes are queued: a melody, not a chord") {
    val (p, c) = player()
    p.playNote(60, 300, 80); p.playNote(64, 600, 80); p.playNote(67, 300, 80)
    close(c.starts, Seq(0.0, 0.3, 0.9))
    close(c.stops, Seq(0.3, 0.9, 1.2))
  }

  test("if the player is idle the note starts at once, if it is playing it starts at the end of the previous one") {
    val (p, c) = player()
    p.playNote(60, 300, 80)            // 0.0 - 0.3
    c.clock(0.1)
    p.playNote(62, 100, 80)            // while playing: at the end of the previous, 0.3 - 0.4
    c.clock(5.0)
    p.playNote(64, 100, 80)            // after a long silence: at once, 5.0 - 5.1
    close(c.starts, Seq(0.0, 0.3, 5.0))
    close(c.stops, Seq(0.3, 0.4, 5.1))
  }

  test("changing the instrument affects only the notes that come after") {
    val (p, c) = player()
    p.playNote(60, 100, 80)
    p.setInstrument(Instrument.ACOUSTIC_BASS)
    p.playNote(40, 100, 80)
    c.types shouldBe Seq("triangle", "sine")
    c.oscillators(1).frequency.value.asInstanceOf[Double] shouldBe NotePlayer.frequency(40) +- 1e-9
  }

  test("stop silences the queued notes and resets the cursor (stopNotePlayer)") {
    val (p, c) = player()
    p.playNote(60, 1000, 80); p.playNote(62, 1000, 80)
    p.stop()
    c.silenced shouldBe 2
    c.clock(0.5)
    p.playNote(64, 100, 80)            // not behind the old queue (2.0): at once
    c.starts.last shouldBe 0.5 +- 1e-9
  }

  test("stop silences EVERY oscillator of a chord (of a playTogether group) and resets the cursor") {
    val (p, c) = player()
    p.playChord(Seq(60, 64, 67, 71), 1000, 80)
    p.playTogether(Seq((48, 500), (52, 2000)), 80)
    p.stop()
    c.silenced shouldBe 6
    c.clock(0.2)
    p.playNote(72, 100, 80)
    c.starts.last shouldBe 0.2 +- 1e-9
  }

  test("the limits are the same as on the desktop; a negative duration does not move the cursor back") {
    val (p, c) = player()
    an[IllegalArgumentException] should be thrownBy p.playNote(128, 10, 80)
    an[IllegalArgumentException] should be thrownBy p.playNote(60, 10, -1)
    an[IllegalArgumentException] should be thrownBy p.setInstrument(128)
    p.playNote(60, 200, 80); p.playNote(60, -50, 80); p.playNote(62, 100, 80)
    close(c.starts, Seq(0.0, 0.2, 0.2))
    // the end of a note with a negative duration does not fall before its start (it starts from `start`)
    close(c.stops, Seq(0.2, 0.2, 0.3))
  }

  test("playNote, setNoteInstrument and stopNotePlayer of Builtins share one cursor and one player") {
    val (b, c) = builtinsWithFake()
    b.setNoteInstrument(b.Instrument.ACOUSTIC_BASS)
    b.playNote(50, 150)
    b.playNote(45, 200)
    b.setNoteInstrument(b.Instrument.PIANO)
    b.playNote(60, 100)
    close(c.starts, Seq(0.0, 0.15, 0.35))
    c.types shouldBe Seq("sine", "sine", "triangle")
    an[IllegalArgumentException] should be thrownBy b.setNoteInstrument(200)
    b.stopNotePlayer()
    c.silenced shouldBe 3
    b.noteTimeLeftMillis shouldBe 0
  }

  test("playTogether: all start at the same moment, each for its own duration; the next note starts at the end of the longest") {
    val (p, c) = player()
    p.playTogether(Seq((60, 300), (64, 1000), (67, 600)), 80)
    p.playNote(72, 100, 80)
    close(c.starts, Seq(0.0, 0.0, 0.0, 1.0))
    close(c.stops, Seq(0.3, 1.0, 0.6, 1.1))
    close(c.frequencies, Seq(60, 64, 67, 72).map(NotePlayer.frequency))
  }

  test("playChord: all of the same duration; the special case of playTogether") {
    val (p, c) = player()
    p.playNote(40, 200, 80)                 // 0.0 - 0.2
    c.clock(0.05)
    p.playChord(Seq(48, 52, 55), 400, 80)   // while playing: at the end of the previous, 0.2 - 0.6, the three together
    p.playNote(60, 100, 80)                 // 0.6 - 0.7
    close(c.starts, Seq(0.0, 0.2, 0.2, 0.2, 0.6))
    close(c.stops, Seq(0.2, 0.6, 0.6, 0.6, 0.7))
  }

  test("the volume of a chord is divided by the square root of the number of notes; one note is as before") {
    val (p, c) = player()
    p.playNote(60, 100, 127)                           // one note: divisor 1
    p.playChord(Seq(60, 64, 67, 71), 100, 127)         // four notes: divisor 2
    val single = c.levels.head
    single shouldBe 0.3 +- 1e-9
    c.levels.tail.foreach(d => d shouldBe (single / 2) +- 1e-9)
    // the power is kept: the sum of the squares of the levels = the power of one note
    c.levels.tail.map(d => d * d).sum shouldBe (single * single) +- 1e-9
  }

  test("peak safety: the total level never passes 0.95 in any group; 0.3/√n up to n <= 10") {
    for (k <- 1 to 16) {
      val (p, c) = player()
      p.playChord(60 until (60 + k), 100, 127)
      c.levels.size shouldBe k
      c.levels.sum should be <= (0.95 + 1e-9)
      if (k <= 10) c.levels.foreach(d => d shouldBe (0.3 / math.sqrt(k)) +- 1e-9)
      else c.levels.foreach(d => d shouldBe (0.95 / k) +- 1e-9)
    }
    succeed
  }

  test("volume 0 plays with a tiny floor (exponentialRamp cannot start from zero)") {
    val (p, c) = player()
    p.playNote(60, 100, 0)
    c.levels.head shouldBe 1e-4 +- 1e-12
  }

  test("if the context is suspended playNote, playChord and playRest resume it; if it is not, they do not touch it") {
    val (p, c) = player()
    p.playNote(60, 100, 80)
    c.resumed shouldBe 0
    c.suspend()
    p.playNote(62, 100, 80); p.playChord(Seq(64, 67), 100, 80); p.playRest(100)
    c.resumed shouldBe 3
    // even if playRest is the first call it resumes the context (so that the next note is heard)
    val (p2, c2) = player()
    c2.suspend()
    p2.playRest(100)
    c2.resumed shouldBe 1
  }

  test("playTogether checks ALL first: if one is out of range no note is queued; an empty sequence does nothing") {
    val (p, c) = player()
    an[IllegalArgumentException] should be thrownBy p.playTogether(Seq((60, 100), (128, 100)), 80)
    an[IllegalArgumentException] should be thrownBy p.playChord(Seq(60, -1), 100, 80)
    an[IllegalArgumentException] should be thrownBy p.playChord(Seq(60, 64), 100, 128)
    c.oscillators shouldBe empty
    p.playTogether(Seq.empty, 80)
    p.playChord(Seq.empty, 100, 80)
    c.oscillators shouldBe empty
    // an empty sequence does not make the context either (making an AudioContext counts as a side effect)
    val fresh = new NotePlayer
    fresh.playTogether(Seq.empty, 80); fresh.playChord(Seq.empty, 100, 80)
    (fresh.audioContext == null) shouldBe true
    p.playNote(62, 100, 80)            // the cursor has not moved: at once
    close(c.starts, Seq(0.0))
  }

  test("playRest waits silently; when idle it counts from now, when playing from the end of the previous note") {
    val (p, c) = player()
    p.playNote(60, 100, 80)            // 0.0 - 0.1
    p.playRest(200)                    // the cursor is 0.3
    p.playNote(62, 100, 80)            // 0.3 - 0.4
    c.clock(5.0)
    p.playRest(100)                    // idle: from 5.0, the cursor is 5.1
    p.playNote(64, 100, 80)            // 5.1 - 5.2
    p.playRest(-50)                    // a negative duration does not take the cursor back
    p.playNote(65, 100, 80)            // 5.2 - 5.3
    close(c.starts, Seq(0.0, 0.3, 5.1, 5.2))
    c.oscillators.size shouldBe 4      // the rest made no oscillator
  }

  test("timeLeftMillis: the time until the queued notes are finished; 0 when idle and when nothing was ever played") {
    val (p, c) = player()
    p.timeLeftMillis shouldBe 0
    p.playNote(60, 300, 80); p.playNote(64, 600, 80); p.playNote(67, 300, 80) // 1200 ms in all
    p.timeLeftMillis shouldBe 1200
    c.clock(0.5)
    p.timeLeftMillis shouldBe 700
    p.playRest(300)
    p.timeLeftMillis shouldBe 1000
    c.clock(9.0)
    p.timeLeftMillis shouldBe 0        // it does not go below zero
    p.stop()
    p.timeLeftMillis shouldBe 0
    // if the context was never made (no Web Audio) it is 0
    new NotePlayer().timeLeftMillis shouldBe 0
  }

  test("timeLeftMillis rounds to the nearest millisecond (not up)") {
    val (p, c) = player()
    p.playNote(60, 300, 80)
    c.clock(0.0006)                    // 299.4 ms left
    p.timeLeftMillis shouldBe 299
    c.clock(0.0001)                    // 299.9 ms left
    p.timeLeftMillis shouldBe 300
  }

  test("playChord, playTogether, playRest and noteTimeLeftMillis of Builtins go to the same player; the default volume is 80") {
    val (b, c) = builtinsWithFake()
    b.playChord(Seq(60, 64), 100)                 // 0.0 - 0.1
    b.playTogether(Seq((72, 100), (76, 300)))     // 0.1 - 0.4
    b.playRest(100)                               // 0.5
    b.playNote(60, 100)                           // 0.5 - 0.6
    close(c.starts, Seq(0.0, 0.0, 0.1, 0.1, 0.5))
    // the ORDER of the notes and the duration of each are bound to the right call: if the pair (72, 100),
    // (76, 300) of playTogether were swapped only the order of the frequencies/ends would change
    close(c.frequencies, Seq(60, 64, 72, 76, 60).map(NotePlayer.frequency))
    close(c.stops, Seq(0.1, 0.1, 0.2, 0.4, 0.6))
    // the default volume is 80 on all the calls: a group of two is 80/127·0.3/√2, a single note 80/127·0.3
    val d1 = 80 / 127.0 * 0.3
    val d2 = d1 / math.sqrt(2)
    close(c.levels, Seq(d2, d2, d2, d2, d1))
    b.noteTimeLeftMillis shouldBe 600
    intercept[IllegalArgumentException](b.playTogether(Seq((60, 100), (-1, 100)))).getMessage should include("Note pitch")
    c.oscillators.size shouldBe 5      // the bad call queued nothing
    // the volume parameter reaches the player
    b.playChord(Seq(60, 64), 100, 40); b.playTogether(Seq((60, 100), (64, 100)), 40)
    c.levels.drop(5).foreach(d => d shouldBe (40 / 127.0 * 0.3 / math.sqrt(2)) +- 1e-9)
    succeed
  }

  test("any collection.Seq goes into playChord and playTogether, mutable ones too (not only an immutable Seq)") {
    val (b, c) = builtinsWithFake()
    b.playChord(mutable.ArrayBuffer(72, 76), 100)        // 0.0 - 0.1
    b.playTogether(mutable.ArrayBuffer((79, 100)))       // 0.1 - 0.2
    close(c.starts, Seq(0.0, 0.0, 0.1))
    close(c.frequencies, Seq(72, 76, 79).map(NotePlayer.frequency))
    b.noteTimeLeftMillis shouldBe 200
  }

  test("a real audio engine (OfflineAudioContext): the three notes of a chord are heard together, the next note after the chord") {
    val Offline = js.Dynamic.global.OfflineAudioContext
    if (js.isUndefined(Offline)) cancel("there is no OfflineAudioContext")
    val sampleRate = 44100
    val ctx = js.Dynamic.newInstance(Offline)(1, (sampleRate * 1.2).toInt, sampleRate)
    val p = new NotePlayer
    p.audioContext = ctx
    val chord = Seq(60, 64, 67)
    p.playChord(chord, 400, 80)   // 0.0 - 0.4, the three together
    p.playRest(200)               // 0.4 - 0.6 silent
    p.playNote(72, 300, 80)       // 0.6 - 0.9

    val promise = ctx.startRendering().asInstanceOf[js.Promise[js.Dynamic]]
    promise.toFuture.map { buffer =>
      val a = buffer.getChannelData(0).asInstanceOf[js.typedarray.Float32Array]
      def power(t0: Double, t1: Double, pitch: Int): Double = {
        val f = NotePlayer.frequency(pitch)
        val k = 2 * math.cos(2 * math.Pi * f / sampleRate)
        var s1 = 0.0; var s2 = 0.0; var i = (t0 * sampleRate).toInt
        val end = (t1 * sampleRate).toInt
        while (i < end) { val x = a(i) + k * s1 - s2; s2 = s1; s1 = x; i += 1 }
        math.sqrt(s1 * s1 + s2 * s2 - k * s1 * s2) / (end - (t0 * sampleRate).toInt)
      }
      val all = chord :+ 72
      def heard(t0: Double, t1: Double): Seq[Int] = {
        val powers = all.map(pitch => pitch -> power(t0, t1, pitch))
        val strongest = powers.map(_._2).max
        if (strongest < 1e-6) Seq.empty else powers.collect { case (pitch, w) if w > strongest * 0.2 => pitch }
      }
      val measured = Seq(
        "chord 0.02-0.30" -> heard(0.02, 0.30),
        "rest 0.45-0.58" -> heard(0.45, 0.58),
        "72 0.65-0.85" -> heard(0.65, 0.85),
        "end 0.95-1.15" -> heard(0.95, 1.15)
      )
      withClue(measured.mkString("\n", "\n", "\n")) {
        measured(0)._2 shouldBe chord      // the three together, no 72
        measured(1)._2 shouldBe empty      // the rest: silent
        measured(2)._2 shouldBe Seq(72)    // only the next note
        measured(3)._2 shouldBe empty      // the total length is 0.9
      }
    }
  }

  test("a real audio engine (OfflineAudioContext): 16 in-phase notes at volume 127, no clipping") {
    val Offline = js.Dynamic.global.OfflineAudioContext
    if (js.isUndefined(Offline)) cancel("there is no OfflineAudioContext")
    val sampleRate = 44100
    val ctx = js.Dynamic.newInstance(Offline)(1, (sampleRate * 0.4).toInt, sampleRate)
    val p = new NotePlayer
    p.audioContext = ctx
    p.setInstrument(Instrument.TRUMPET)        // a square wave: the highest peak
    p.playChord(Seq.fill(16)(60), 300, 127)    // the same pitch: all the oscillators are in phase
    val promise = ctx.startRendering().asInstanceOf[js.Promise[js.Dynamic]]
    promise.toFuture.map { buffer =>
      val a = buffer.getChannelData(0).asInstanceOf[js.typedarray.Float32Array]
      var peak = 0.0; var i = 0
      while (i < a.length) { peak = math.max(peak, math.abs(a(i).toDouble)); i += 1 }
      withClue(f"peak $peak%.3f -- ") {
        NotePlayer.waveform(Instrument.TRUMPET) shouldBe "square"
        peak should be > 0.5               // there really is sound
        peak should be <= 0.96
      }
    }
  }

  test("a real audio engine (OfflineAudioContext): one note is heard in each window") {
    val Offline = js.Dynamic.global.OfflineAudioContext
    if (js.isUndefined(Offline)) cancel("there is no OfflineAudioContext")
    val sampleRate = 44100
    val ctx = js.Dynamic.newInstance(Offline)(1, (sampleRate * 1.6).toInt, sampleRate)
    val p = new NotePlayer
    p.audioContext = ctx
    val notes = Seq(60 -> 300, 64 -> 600, 67 -> 300) // 0-0.3, 0.3-0.9, 0.9-1.2 s
    notes.foreach { case (pitch, ms) => p.playNote(pitch, ms, 80) }

    val promise = ctx.startRendering().asInstanceOf[js.Promise[js.Dynamic]]
    promise.toFuture.map { buffer =>
      val a = buffer.getChannelData(0).asInstanceOf[js.typedarray.Float32Array]
      // Goertzel: the power of the frequency f in the samples [i0, i1)
      def power(i0: Int, i1: Int, f: Double): Double = {
        val k = 2 * math.cos(2 * math.Pi * f / sampleRate)
        var s1 = 0.0; var s2 = 0.0; var i = i0
        while (i < i1) { val x = a(i) + k * s1 - s2; s2 = s1; s1 = x; i += 1 }
        math.sqrt(s1 * s1 + s2 * s2 - k * s1 * s2) / (i1 - i0)
      }
      val windows = Seq((0.00, 0.25, 60), (0.35, 0.85, 64), (0.95, 1.15, 67))
      val measured = windows.map { case (t0, t1, expected) =>
        val powers = notes.map { case (pitch, _) =>
          pitch -> power((t0 * sampleRate).toInt, (t1 * sampleRate).toInt, NotePlayer.frequency(pitch))
        }
        val strongest = powers.map(_._2).max
        val heard = powers.collect { case (pitch, w) if w > strongest * 0.2 => pitch }
        (f"$t0%.2f-$t1%.2f s", expected, heard, powers)
      }
      withClue(measured.mkString("\n", "\n", "\n")) {
        measured.foreach { case (_, expected, heard, _) => heard shouldBe Seq(expected) }
        // silence after 1.2 s: the total length is as on the desktop
        power((1.3 * sampleRate).toInt, (1.55 * sampleRate).toInt, NotePlayer.frequency(67)) shouldBe 0.0 +- 1e-6
      }
    }
  }
}
