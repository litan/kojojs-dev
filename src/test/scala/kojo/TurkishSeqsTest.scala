package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishSeqs
    extends kojo.tr.SayıYöntemleri
    with kojo.tr.DizinYöntemleri
    with kojo.tr.YöneyYöntemleri
    with kojo.tr.DizimYöntemleri
    with kojo.tr.YinelenebilirYöntemleri
    with kojo.tr.EşlemYöntemleri
    with kojo.tr.KümeYöntemleri
    with kojo.tr.AralıkYöntemleri
    with kojo.tr.MiskinDizinYöntemleri
    with kojo.tr.DiziYöntemleri
    with kojo.tr.DizikYöntemleri
    with kojo.tr.BelkiYöntemleri

class TurkishSeqsTest extends AnyFunSuite with Matchers {
  import TurkishSeqs._

  test("dizi: the Turkish names of Seq") {
    val d = Dizi(3, 1, 2)
    d.boyu should be(3)
    d.başı should be(3)
    d.sonu should be(2)
    d.kuyruğu should be(Seq(1, 2))
    d.önü should be(Seq(3, 1))
    d.sıralı should be(Seq(1, 2, 3))
    d.tersi should be(Seq(2, 1, 3))
    d.işle(_ * 2) should be(Seq(6, 2, 4))
    d.ele(_ > 1) should be(Seq(3, 2))
    d.indirge(_ + _) should be(6)
    d.topla should be(6)
    d.çarp should be(6)
    d.enİrisi should be(3)
    d.enUfağı should be(1)
    d.enİrisiİşlevle(x => -x) should be(1)
    d.soldanKatla(0)(_ + _) should be(6)
    d.yazıYap("-") should be("3-1-2")
    d.içeriyorMu(2) should be(doğru)
    d.sırası(1) should be(1)
    d.al(2) should be(Seq(3, 1))
    d.düşür(1) should be(Seq(1, 2))
    d.değiştir(0, 9) should be(Seq(9, 1, 2))
    d.say(_ > 1) should be(2)
    d.böl(_ > 1) should be((Seq(3, 2), Seq(1)))
    d.ikileSırayla should be(Seq((3, 0), (1, 1), (2, 2)))
    d.sırala(x => x) should be(Seq(1, 2, 3))
    d.dizine should be(List(3, 1, 2))
    d.kümeye should be(Set(1, 2, 3))

    Dizi(1, 1, 2, 2, 3).yinelemesiz should be(Seq(1, 2, 3))
    Dizi("aa", "ab", "bc").yinelemesizİşlevle(_.head) should be(Seq("aa", "bc"))
    Dizi.doldur(4)(i => i * i) should be(Seq(0, 1, 4, 9))
    Dizi.boş[Sayı].boşMu should be(doğru)

    Diz(1, 2).boyu should be(2)

    // yineleyici
    Dizi(1, 2, 3).öbekli(2).dizine should be(List(Seq(1, 2), Seq(3)))
  }

  test("dizi: an out-of-bounds access throws (the Scala.js varargs trap)") {
    an[IndexOutOfBoundsException] should be thrownBy Dizi(1, 2, 3)(5)
    an[IndexOutOfBoundsException] should be thrownBy Dizi(1)(-1)
    an[IndexOutOfBoundsException] should be thrownBy Diz(1, 2)(7)
    Dizi(1, 2, 3)(2) should be(3)
    Diz(1, 2)(1) should be(2)
    Dizi(1, 2, 3).toString should be("List(1, 2, 3)")
    Diz(1, 2).toString should be("List(1, 2)")
  }

  test("dizik: the Dizik/EsnekDizik constructors of the desktop") {
    val d = Dizik(3, 1, 2)
    d.length shouldBe 3
    Dizik.boş[Sayı](4).length shouldBe 4
    Dizik.boş[Sayı]().length shouldBe 0
    Dizim.boş[Sayı]().boyu shouldBe 0
    Dizim.boş[Sayı](3).boyu shouldBe 3
    Dizim.boş[Sayı](2, 3).boyu shouldBe 2
    Dizik.boş[Sayı](2, 3).map(_.length).sum shouldBe 6
    Dizik.doldur(2, 3)(7).map(_.sum).sum shouldBe 42
    val e = EsnekDizik(1, 2)
    e += 3
    e.toList shouldBe List(1, 2, 3)
    EsnekDizik.diziden(List("a", "b")).length shouldBe 2
    EsnekDizik.doldur(2)("x").toList shouldBe List("x", "x")
  }

  test("Dizik: the Turkish methods of Array (ArrayMethods of the desktop)") {
    val ızgara = Dizim.doldur[Yazı](2, 3)("boş")
    val satır = ızgara(1)
    satır.diziye shouldBe Seq("boş", "boş", "boş")
    satır.boyu shouldBe 3

    val d: Dizik[Sayı] = Dizik(3, 1, 2)
    d.başı shouldBe 3
    d.sonu shouldBe 2
    d.sıralı.diziye shouldBe Seq(1, 2, 3)
    d.ele(_ > 1).diziye shouldBe Seq(3, 2)
    d.işle(_ * 2).diziye shouldBe Seq(6, 2, 4)
    d.topla shouldBe 6
    d.enİrisi shouldBe 3
    d.enUfağı shouldBe 1
    d.say(_ > 1) shouldBe 2
    d.içeriyorMu(2) shouldBe true
    d.sırası(1) shouldBe 1
    d.tersi.diziye shouldBe Seq(2, 1, 3)
    d.yazıYap("-") shouldBe "3-1-2"
    d.dizine shouldBe List(3, 1, 2)
    d.yöneye shouldBe Vector(3, 1, 2)
    d.kümeye shouldBe Set(1, 2, 3)
    d.dizime.diziye shouldBe Seq(3, 1, 2)
    d.değiştirYerinde(0, 9)
    d(0) shouldBe 9
  }

  test("Yineleyici: the Turkish names of Iterator; Dizi/Diz behave like Scala") {
    Dizi(1, 2, 3).toString shouldBe "List(1, 2, 3)"
    Diz(1, 2, 3).toString shouldBe "List(1, 2, 3)"
    Dizi(1, 2, 3) shouldBe Dizin(1, 2, 3)
    Diz.boş[Sayı].boyu shouldBe 0

    Dizi(1, 2, 3, 4).öbekli(2).dizine shouldBe List(Seq(1, 2), Seq(3, 4))
    val y = Dizi(1, 2, 3).yineleyici
    y.dahaVarMı shouldBe true
    y.sıradaki shouldBe 1
    y.dizine shouldBe List(2, 3)
    y.dahaVarMı shouldBe false

    Dizi(1, 2, 3).yineleyici.işle(_ * 2).dizine shouldBe List(2, 4, 6)
    Dizi(1, 2, 3).yineleyici.ele(_ > 1).diziye shouldBe Seq(2, 3)
    Dizi(1, 2, 3).yineleyici.topla shouldBe 6
    Dizi(1, 2, 3).yineleyici.katla(10)(_ + _) shouldBe 16
    Dizi(1, 2, 3).yineleyici.bul(_ > 1) shouldBe Some(2)
    Dizi(1, 2).yineleyici.yazıYap("-") shouldBe "1-2"
    Dizi(1, 2).yineleyici.kümeye shouldBe Set(1, 2)

    val (a, b) = Dizi(1, 2, 3).yineleyici.ikizYap
    a.dizine shouldBe List(1, 2, 3)
    b.dizine shouldBe List(1, 2, 3)

    val bi = Dizi(1, 2, 3).yineleyici.bellekli
    bi.head shouldBe 1
    bi.dizine shouldBe List(1, 2, 3)

    Dizi(1, 2).yineleyici.gösterdikleriAynıMı(Dizi(1, 2)) shouldBe true
    Dizi(1, 2).yineleyici.gösterdikleriAynıMı(Dizi(2, 1)) shouldBe false
  }
}
