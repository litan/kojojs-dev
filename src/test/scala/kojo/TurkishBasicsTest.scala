package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** Behavior of the Turkish basics: numbers, math, Belki (Option) and Nokta (point). Compiling only proves
  * the types; that the hand-written functions (gcd, lcm, mean...) are right shows only when they run.
  */
object TurkishBasics
    extends kojo.tr.SayıYöntemleri
    with kojo.tr.MatematikYöntemleri
    with kojo.tr.BelkiYöntemleri
    with kojo.tr.NoktaYöntemleri

class TurkishBasicsTest extends AnyFunSuite with Matchers {
  import TurkishBasics._

  test("math: the hand-written functions (instead of Commons Math)") {
    enİriOrtakPayda(12, 18) should be(6)
    enİriOrtakPayda(18, 12) should be(6)
    enİriOrtakPayda(-12, 18) should be(6)
    enİriOrtakPayda(7, 13) should be(1)
    enİriOrtakPayda(0, 5) should be(5)

    enUfakOrtakKat(4, 6) should be(12)
    enUfakOrtakKat(21, 6) should be(42)
    enUfakOrtakKat(0, 5) should be(0)

    ortalama(Array(1.0, 2.0, 3.0)) should be(2.0)
    ortalama(Array(5.0)) should be(5.0)
    // sample variance (divides by n-1)
    değişim(Array(1.0, 2.0, 3.0, 4.0)) should be(1.6666666666666667 +- 1e-12)
  }

  test("math: geometry and basic functions") {
    karekökü(16.0) should be(4.0)
    karesi(3.0) should be(9.0)
    kuvveti(2.0, 10.0) should be(1024.0)
    uzaklık(0.0, 0.0, 3.0, 4.0) should be(5.0)
    açı(0.0, 0.0, 1.0, 1.0) should be(45.0 +- 1e-9)
    açı(0.0, 0.0, 0.0, 1.0) should be(90.0 +- 1e-9)
    yuvarla(3.14159, 2) should be(3.14)
    yuvarla(2.5) should be(3.0)
    işareti(-7.0) should be(-1)
    işareti(0.0) should be(0)
    dereceye(math.Pi) should be(180.0 +- 1e-9)
  }

  test("number extensions") {
    (5).yazıya should be("5")
    (5).kesire should be(5.0)
    (-3).mutlakDeğer should be(3)
    (2).enİrisi(7) should be(7)
    (2).enUfağı(7) should be(2)
    (65).harfe should be('A')

    (1 |-| 3).toList should be(List(1, 2, 3)) // closed range
    (1 |- 3).toList should be(List(1, 2)) // half open

    (3.7).taban should be(3.0)
    (3.2).tavan should be(4.0)
    (3.6).yakın should be(4L)

    Sayılar(3, 1, 2).sorted should be(Vector(1, 2, 3))
    Sayılar().length should be(0)
    Sayı.Enİrisi should be(Int.MaxValue)
    Kesir.EnUfağı should be(Double.MinValue)
  }

  test("belki: the Turkish names of Option") {
    val b: Belki[Sayı] = Biri(5)
    b.varMı should be(doğru)
    b.yokMu should be(yanlış)
    b.al should be(5)
    b.işle(_ * 2) should be(Some(10))
    b.alYoksa(0) should be(5)
    b.ele(_ > 10) should be(None)
    b.ele(_ > 1) should be(Some(5))
    b.dizine should be(List(5))

    val h: Belki[Sayı] = Hiçbiri
    h.yokMu should be(doğru)
    h.alYoksa(42) should be(42)
    varMı(h) should be(yanlış)
    yokMu(h) should be(doğru)

    // pattern matching works
    (Biri(7) match { case Some(n) => n; case None => -1 }) should be(7)
  }

  test("nokta: a Nokta is a mutable PIXI point and kopyası makes a separate one") {
    val n = Nokta(3, 4)
    val k = n.kopyası
    n.x = 10
    k.x should be(3.0)
    n.uzaklığı(Nokta(10, 0)) should be(4.0)
    Nokta.sıfır.x should be(0.0)
    (n match { case Nokta(x, y) => (x, y) }) should be((10.0, 4.0))
    Dikdörtgen(0, 0, 10, 20).içeriyorMu(Nokta(5, 5)) should be(doğru)
  }

  test("a script can use the Turkish basics through trTurtle") {
    implicit val kojoWorld: TestKojoWorld = new TestKojoWorld()
    val builtins = new kojo.syntax.Builtins()
    import builtins.trTurtle._
    karekökü(81.0) should be(9.0)
    (5).mutlakDeğer should be(5)
    Biri(2).alYoksa(0) should be(2)
  }
}
