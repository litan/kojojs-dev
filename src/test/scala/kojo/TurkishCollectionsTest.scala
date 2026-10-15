package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishColls
    extends kojo.tr.SayıYöntemleri
    with kojo.tr.AralıkYöntemleri
    with kojo.tr.DizimYöntemleri
    with kojo.tr.DizinYöntemleri
    with kojo.tr.MiskinDizinYöntemleri
    with kojo.tr.YöneyYöntemleri
    with kojo.tr.Yöney2BYöntemleri

class TurkishCollectionsTest extends AnyFunSuite with Matchers {
  import TurkishColls._

  test("aralık: the Turkish names of Range") {
    val a = Aralık(1, 5)
    a.boyu should be(4)
    a.başı should be(1)
    a.sonu should be(4)
    a.dizine should be(List(1, 2, 3, 4))
    a.içindeMi(3) should be(doğru)
    a.içindeMi(5) should be(yanlış)
    a.indirge(_ + _) should be(10)
    a.işle(_ * 2).toList should be(List(2, 4, 6, 8))
    // Aralık is `type Aralık = Range`: toString CANNOT be overridden (Range's is "Range 1 until 5").
    // The display for learners is yazı()/yazıya.
    a.yazıya should be("Aralık(1, 2, 3, 4)")

    Aralık.kapalı(1, 5).dizine should be(List(1, 2, 3, 4, 5))
    Aralık(0, 10, 2).dizine should be(List(0, 2, 4, 6, 8))
    // a long range is written shortened
    Aralık(1, 101).yazıya should include("...")

    (1 to 5).boyu should be(5)
    (1 to 10).adım(3).toList should be(List(1, 4, 7, 10))
    (1 to 4).soldanKatla(0)(_ + _) should be(10)
  }

  test("dizin: the Turkish names of List") {
    val l = Dizin(3, 1, 2)
    l.boyu should be(3)
    l.başı should be(3)
    l.sıralı should be(List(1, 2, 3))
    l.tersi should be(List(2, 1, 3))
    l.işle(_ * 2) should be(List(6, 2, 4))
    l.düzİşle(x => Dizin(x, x)) should be(List(3, 3, 1, 1, 2, 2))
    l.topla should be(6)
    l.enİrisi should be(3)
    l.ele(_ > 1) should be(List(3, 2))
    l.böl(_ > 1) should be((List(3, 2), List(1)))
    l.ikileSırayla should be(List((3, 0), (1, 1), (2, 2)))
    l.yazıYap("+") should be("3+1+2")
    Dizin(1, 1, 2).yinelemesiz should be(List(1, 2))
    Dizin.doldur(3)(i => i + 1) should be(List(1, 2, 3))
    Dizin.boş[Sayı].boşMu should be(doğru)
    Boş should be(Nil)
  }

  test("yöney: the Turkish names of Vector") {
    val v = Yöney(3, 1, 2)
    v.boyu should be(3)
    v.sıralı should be(Vector(1, 2, 3))
    v.işle(_ + 1) should be(Vector(4, 2, 3))
    v.topla should be(6)
    v.çarp should be(6)
    v.tersi should be(Vector(2, 1, 3))
    v.değiştir(0, 9) should be(Vector(9, 1, 2))
    v.dilim(1, 3) should be(Vector(1, 2))
    v.enUfağı should be(1)
    v.dizine should be(List(3, 1, 2))
    Yöney.boş[Sayı].boşMu should be(doğru)
    Yöney.doldur(3)(7) should be(Vector(7, 7, 7))
  }

  test("miskin dizin: the lazy sequence") {
    val m = MiskinDizin.sayalım(1).al(5)
    m.dizine should be(List(1, 2, 3, 4, 5))
    MiskinDizin.sayalım(0, 2).al(4).dizine should be(List(0, 2, 4, 6))
    MiskinDizin.yinele(1)(_ * 2).al(5).dizine should be(List(1, 2, 4, 8, 16))
    MiskinDizin.doldur(3)(7).dizine should be(List(7, 7, 7))
    MiskinDizin.boş[Sayı].boşMu should be(doğru)
    // lazy filtering of an infinite sequence
    MiskinDizin.sayalım(1).ele(_ % 3 == 0).al(3).dizine should be(List(3, 6, 9))
    MiskinDizin.sayalım(1).işle(x => x * x).al(3).topla should be(1 + 4 + 9)
    MiskinDizin.sayalım(1).alDoğruKaldıkça(_ < 4).dizine should be(List(1, 2, 3))
  }

  test("Aralık display: yazıya uses the same body as the display") {
    // Aralık is a type alias, so toString cannot be overridden; the display for learners is in
    // Aralık.gösterim and yazıya calls it. Here the body is tested directly.
    Aralık.gösterim(Aralık(1, 6)) should be("Aralık(1, 2, 3, 4, 5)")
    Aralık.gösterim(Aralık(1, 200, 7)) should be(
      "Aralık(1, 8, 15, 22, 29 ... 169, 176, 183, 190, 197)")
    Aralık.gösterim(Aralık(1, 1)) should be("Aralık()")
    // exactly 10 elements is the shortening threshold: all of them are written
    Aralık.gösterim(Aralık(10, 0, -1)) should be("Aralık(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)")
    // EXACTLY the same as yazıya (one implementation)
    Aralık(1, 200, 7).yazıya should be(Aralık.gösterim(Aralık(1, 200, 7)))
    (1 to 5).yazıya should be(Aralık.gösterim(1 to 5))
  }

  test("dizim: the Array wrappers") {
    val d = Dizim(1, 2, 3)
    d.boyu should be(3)
    d(0) should be(1)
    d.dizine should be(List(1, 2, 3))
    Dizim.doldur(3)(5).dizine should be(List(5, 5, 5))

    val e = EsnekDizim(1, 2, 3)
    e.sayı should be(3)
    e += 4
    e.sayı should be(4)
    e.eleYerinde(_ % 2 == 0)
    e.dizine should be(List(2, 4))
    e.çıkar(0)
    e.dizine should be(List(4))
  }


  test("Dizim: multi-dimensional boş/doldur, the inner layer is an Array (tic-tac-toe, genart-tri-mesh)") {
    // 2-dimensional doldur: as in tic-tac-toe's `Dizim.doldur[Hane](3, 3)(Boş)`
    val tahta = Dizim.doldur[Yazı](3, 3)("boş")
    tahta.boyu shouldBe 3
    tahta(0).length shouldBe 3
    tahta(1)(2) shouldBe "boş"
    // the main point: `tahta(x)(y) = değer` must compile (Array.update)
    tahta(1)(2) = "insan"
    tahta(1)(2) shouldBe "insan"
    tahta(0)(2) shouldBe "boş" // the rows are not shared

    // 2-dimensional boş: as in genart-tri-mesh's `Dizim.boş[Nokta](n + 2, n + 2)`
    val n = Dizim.boş[Sayı](2, 3)
    n.boyu shouldBe 2
    n(0).length shouldBe 3
    n(1)(0) = 7
    n(1)(0) shouldBe 7

    // 3-dimensional, the third overload of the desktop
    val ü = Dizim.doldur[Sayı](2, 2, 2)(0)
    ü(1)(1)(1) = 5
    ü(1)(1)(1) shouldBe 5
    // the intermediate value is needed: with `Dizim.boş[Sayı](1, 1, 1)(0)` Scala 2 takes `(0)` for the
    // implicit ClassTag list (the same trap as the note on `boş()` at the top of dizim.scala)
    val ü3 = Dizim.boş[Sayı](1, 1, 1)
    ü3(0)(0).length shouldBe 1

    // the 1-dimensional signatures are intact
    Dizim.doldur[Sayı](3)(9).diziye shouldBe Seq(9, 9, 9)
    Dizim.boş[Sayı](2).boyu shouldBe 2
    Dizim.boş[Sayı]().boyu shouldBe 0
  }


  test("Yöney2B: a type alias of Vector2D, with Turkish names") {
    val v = Yöney2B(3, 4)
    v.boyu should be(5.0)
    v.boyunKaresi should be(25.0)
    v.döndür(90).x should be(-4.0 +- 1e-9)
    v.içÇarpım(Yöney2B(1, 0)) should be(3.0)
    Yöney2B.sıfır.boyu should be(0.0)
    Yöney2B.açıdan(90).y should be(1.0 +- 1e-9)
    (v match { case Yöney2B(x, y) => (x, y) }) should be((3.0, 4.0))
    (v: kojo.Vector2D) should be(kojo.Vector2D(3, 4))
  }
}
