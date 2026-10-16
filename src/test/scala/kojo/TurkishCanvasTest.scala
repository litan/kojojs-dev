package kojo

import org.scalajs.dom
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** The Turkish names of the sketch drawing: `Eskiz`, `TuvalÇizim` (= CanvasDraw) and `Yazıyüzü` (= Font). */
class TurkishCanvasTest extends AnyFunSuite with Matchers {
  private implicit val w: TestKojoWorld = new TestKojoWorld()
  private val b = new kojo.syntax.Builtins()
  import b.trTurtle._

  private def alpha(p: SketchPic, x: Int, y: Int): Int = {
    val g = p.canvasElement.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    g.getImageData(x, y, 1, 1).data(3).toInt
  }

  test("an Eskiz subclass with the TuvalÇizim methods (the pattern of the desktop sketch examples)") {
    var loops = 0
    class Trial extends Eskiz {
      var setups = 0
      def kurulum(tuval: TuvalÇizim): Unit = {
        setups += 1
        tuval.artalan(0, 0, 0)
      }
      def çizimDöngüsü(tuval: TuvalÇizim): Unit = {
        loops += 1
        tuval.dönüşümüKaydet()
        tuval.götür(100, 100)
        tuval.fırçasız()
        tuval.boya(255, 0, 0)
        tuval.dikdörtgen(0, 0, 10, 10)
        tuval.dönüşümüGeriYükle()
        tuval.fırça(Renkler.mavi)
        tuval.fırçaAğırlığı(3)
        tuval.fırçaUcu(tuval.projeUç)
        tuval.fırçaBirleşimi(tuval.eğikBirleşim)
        tuval.çizgi(0, 0, 20, 0)
        tuval.elips(200, 200, 10, 10)
        tuval.yay(300, 300, 20, 20, 0, 1)
        tuval.nokta(1, 1)
        tuval.yazıYüzü(yazıyüzü("serif", 12))
        tuval.yazı("a", 5, 400)
        tuval.döndürDerece(10); tuval.döndürRadyan(0.1); tuval.büyüt(2); tuval.büyüt(1, 1)
        tuval.karışımKipi("source-over")
        tuval.şekilBaşla(); tuval.köşe(0, 0); tuval.köşe(5, 0); tuval.köşe(5, 5)
        tuval.şekilBitir()
        tuval.şekilBaşla(); tuval.köşe(0, 0); tuval.bezierKöşesi(1, 1, 2, 2, 3, 3); tuval.şekilBitir()
        tuval.şekilBaşla(); tuval.köşe(0, 0); tuval.ikinciDereceKöşe(1, 1, 2, 0); tuval.şekilBitir()
        tuval.şekilBaşla(); (1 to 4).foreach(i => tuval.eğriKöşesi(i * 3, i * i)); tuval.şekilBitir()
        tuval.şekilBaşla(); tuval.açısalKöşe(10, 0); tuval.açısalKöşe(10, 90); tuval.şekilBitir()
        tuval.rastgeleTohum(1L)
        tuval.döngüdeMi shouldBe true
      }
    }
    val e = new Trial
    val p = new SketchPic(e.kurulum, e.çizimDöngüsü, 1, b.setRandomSeed)
    p.draw()
    p.step()
    e.setups shouldBe 1
    loops shouldBe 1
    alpha(p, 105, 105) shouldBe 255
  }

  test("tuvalEskizi, canlandırTuvalÇizimle and canlandırKurulumluTuvalÇizimle") {
    var setups = 0
    class E extends Eskiz {
      def kurulum(t: TuvalÇizim): Unit = ()
      def çizimDöngüsü(t: TuvalÇizim): Unit = ()
    }
    tuvalEskizi(new E, 1)
    canlandırTuvalÇizimle(t => t.döngüyüDurdur())
    canlandırKurulumluTuvalÇizimle(t => setups += 1)(t => ())
    setups shouldBe 1
  }

  test("Yazıyüzü is the English Font") {
    val yy = yazıyüzü("serif", 12, 3)
    (yy: Font) shouldBe Font("serif", 12, 3)
    yy.ad shouldBe "serif"
    yy.boy shouldBe 12
    yy.kalınMı shouldBe true
    yy.eğikMi shouldBe true
    yazıyüzü("serif", 12).biçem shouldBe 0
    yazıyüzleri should contain("serif")
  }

  test("the Turkish color constructors give the English colors") {
    Renk(255, 0, 0) shouldBe Renkler.kırmızı
    Renk.kym(0, 0, 255) shouldBe Renkler.mavi
    Renk.rgb(0, 128, 0) shouldBe renkKur(0, 128, 0)
    Renk.ada(120, 1.0, 0.5) shouldBe Renk.hsl(120, 1.0, 0.5)
    renkler.red shouldBe Renkler.kırmızı
  }
}
