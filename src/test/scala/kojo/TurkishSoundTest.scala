package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.scalajs.js

class TurkishSoundTest extends AnyFunSuite with Matchers {
  import kojo.syntax.Builtins

  // A fake AudioContext that records when each oscillator starts and stops, and its waveform and frequency
  class FakeContext {
    val oscillators = mutable.ArrayBuffer.empty[js.Dynamic]
    private def node(): js.Dynamic = js.Dynamic.literal(connect = (_: js.Any) => (), disconnect = () => ())
    val ctx: js.Dynamic = js.Dynamic.literal(currentTime = 0.0, destination = node())
    ctx.createOscillator = () => {
      val o = node()
      o.frequency = js.Dynamic.literal(value = 0.0)
      o.start = (t: Double) => { o.startAt = t }
      o.stop = (t: Double) => { o.stopAt = t }
      oscillators += o
      o
    }
    ctx.createGain = () => {
      val g = node()
      g.gain = js.Dynamic.literal(
        setValueAtTime = (_: Double, _: Double) => (),
        exponentialRampToValueAtTime = (_: Double, _: Double) => ()
      )
      g
    }
    def starts: Seq[Double] = oscillators.map(_.startAt.asInstanceOf[Double]).toSeq
    def stops: Seq[Double] = oscillators.map(_.stopAt.asInstanceOf[Double]).toSeq
    def types: Seq[String] = oscillators.map(_.`type`.asInstanceOf[String]).toSeq
  }

  private def close(a: Seq[Double], b: Seq[Double]) = {
    a.size shouldBe b.size
    a.zip(b).foreach { case (x, y) => x shouldBe y +- 1e-9 }
  }

  private def turkishWithFake() = {
    implicit val kojoWorld: KojoWorld = new TestKojoWorld()
    val b = new Builtins()
    val c = new FakeContext
    b.notePlayer.audioContext = c.ctx
    (b, b.trTurtle, c)
  }

  test("notaÇal queues notes as a melody and shares the cursor with playNote") {
    val (b, tr, c) = turkishWithFake()
    tr.notaÇal(60, 300)
    b.playNote(64, 200, 80)
    tr.notaÇal(67, 100)
    close(c.starts, Seq(0.0, 0.3, 0.5))
    close(c.stops, Seq(0.3, 0.5, 0.6))
  }

  test("akorÇal and beraberÇal start the notes together and the next note after the longest") {
    val (_, tr, c) = turkishWithFake()
    tr.akorÇal(Seq(60, 64, 67), 500)
    tr.beraberÇal(Seq((72, 200), (48, 400)))
    tr.notaÇal(60, 100)
    close(c.starts, Seq(0.0, 0.0, 0.0, 0.5, 0.5, 0.9))
    tr.kalanNotaSüresi should be(1000) // the fake clock stays at 0, so the whole queued length is left
  }

  test("notaSus is a rest, notaÇalgısınıKur changes the waveform of the notes that follow") {
    val (_, tr, c) = turkishWithFake()
    tr.notaÇal(60, 100)
    tr.notaSus(200)
    tr.notaÇalgısınıKur(tr.Çalgı.AkustikBas)
    tr.notaÇal(60, 100)
    close(c.starts, Seq(0.0, 0.3))
    c.types shouldBe Seq("triangle", "sine")
  }

  test("notaÇalarıKapat and notaÇalarıDurdur reset the cursor") {
    val (_, tr, c) = turkishWithFake()
    tr.notaÇal(60, 1000)
    tr.notaÇalarıKapat()
    tr.notaÇal(62, 100)
    tr.notaÇalarıDurdur()
    tr.notaÇal(64, 100)
    close(c.starts, Seq(0.0, 0.0, 0.0))
  }

  test("the numbers are checked, with Turkish messages, and nothing is queued by a bad call") {
    val (_, tr, c) = turkishWithFake()
    an[IllegalArgumentException] should be thrownBy tr.notaÇal(128, 100)
    an[IllegalArgumentException] should be thrownBy tr.notaÇal(60, 100, 200)
    an[IllegalArgumentException] should be thrownBy tr.notaÇalgısınıKur(200)
    an[IllegalArgumentException] should be thrownBy tr.akorÇal(Seq(60, 130), 100)
    an[IllegalArgumentException] should be thrownBy tr.beraberÇal(Seq((60, 100), (-1, 100)))
    val e = the[IllegalArgumentException] thrownBy tr.notaÇal(128, 100)
    e.getMessage should include("nota 0 ile 127 arasında olmalı")
    c.oscillators shouldBe empty
  }

  test("the frequency of a pitch and the waveform of an instrument") {
    val (_, tr, _) = turkishWithFake()
    tr.notaFrekansı(69) should be(440.0 +- 1e-9)
    tr.notaFrekansı(81) should be(880.0 +- 1e-9)
    tr.çalgıDalgası(0) should be("triangle")
    tr.çalgıDalgası(32) should be("sine")
  }
}
