package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishSmall
    extends kojo.tr.HarfYöntemleri
    with kojo.tr.İkisindenBiriYöntemleri
    with kojo.tr.BölümselİşlevYöntemleri
    with kojo.tr.KökTürYöntemleri
    with kojo.tr.BuAnYöntemleri

class TurkishSmallTest extends AnyFunSuite with Matchers {
  import TurkishSmall._

  test("the Turkish rule for the dotted and dotless i") {
    // Java's and JS's default is English: 'i'.toUpper = 'I', 'I'.toLower = 'i'.
    // In Turkish i -> İ and I -> ı.
    'i'.büyükHarfe should be('İ')
    'ı'.büyükHarfe should be('I')
    'I'.küçükHarfe should be('ı')
    'İ'.küçükHarfe should be('i')
    // other letters do not change
    'a'.büyükHarfe should be('A')
    'Z'.küçükHarfe should be('z')
  }

  test("harf: the Turkish names of Char") {
    '5'.sayıMı should be(doğru)
    'a'.sayıMı should be(yanlış)
    'a'.harfMi should be(doğru)
    ' '.boşlukMu should be(doğru)
    'a'.küçükHarfMi should be(doğru)
    'A'.büyükHarfMi should be(doğru)
    'A'.sayıya should be(65)
    Harf.sayıMı('7') should be(doğru)
    Harf.enUfağı should be(Char.MinValue)
    Harf.enİrisi should be(Char.MaxValue)
  }

  test("kök türler: object methods") {
    "abc".yazıya should be("abc")
    val n1: Nesne = "x"
    n1.eşitMi("x") should be(doğru)
    val a: HerGönder = "q"
    a.aynıMı(a) should be(doğru)

    // traits with Turkish member names (written with Turkish keywords on the desktop)
    class Nokta(val x: Sayı) extends BaskınYazıyaYöntemiyle {
      def yazıya = s"Nokta($x)"
    }
    new Nokta(3).toString should be("Nokta(3)")
  }

  test("Eşsizlik: equality and hash come from kıymaKodu, and only between Eşsizlik objects") {
    class Anahtar(val k: Sayı) extends Eşsizlik { def kıymaKodu = k }
    new Anahtar(1) should be(new Anahtar(1))
    new Anahtar(1) should not be new Anahtar(2)
    (new Anahtar(1) == "1") should be(yanlış)
    new Anahtar(7).hashCode should be(7)
  }

  test("bölümsel işlev") {
    val bi: Bölümselİşlev[Sayı, Yazı] = { case 1 => "bir"; case 2 => "iki" }
    bi.tanımlıMı(1) should be(doğru)
    bi.tanımlıMı(3) should be(yanlış)
    bi(2) should be("iki")
  }

  test("İkisindenBiri: the Turkish names of Either") {
    val sol: İkisindenBiri[Yazı, Sayı] = Sol("hata")
    val sağ: İkisindenBiri[Yazı, Sayı] = Sağ(5)
    sol.solMu shouldBe true
    sağ.sağMı shouldBe true
    sağ.işle(_ * 2) shouldBe Sağ(10)
    sol.işle(_ * 2) shouldBe Sol("hata")
    sağ.alYoksa(0) shouldBe 5
    sol.alYoksa(0) shouldBe 0
    sağ.belkiye shouldBe Some(5)
    sol.belkiye shouldBe None
    sağ.takasla shouldBe Sol(5)
    sol.katla(h => s"error: $h", d => s"value: $d") shouldBe "error: hata"
    sağ.katla(h => s"error: $h", d => s"value: $d") shouldBe "value: 5"
    val ikisiDeYazı: İkisindenBiri[Yazı, Yazı] = Sol("soldaki")
    ikisiDeYazı.birleştir shouldBe "soldaki"
    İkisindenBiri.koşulla(3 > 2, "oldu", "olmadı") shouldBe Sağ("oldu")
  }

  test("BuAn: the fields are the ones of the current time") {
    val an = BuAn()
    an.ay should (be >= 1 and be <= 12)
    an.gün should (be >= 1 and be <= 31)
    an.saat should (be >= 0 and be <= 23)
    an.yıl should be >= 2026
    an.hepsi should fullyMatch regex """\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}"""
  }
}
