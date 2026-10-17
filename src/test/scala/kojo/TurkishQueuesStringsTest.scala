package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishQueuesStrings
    extends kojo.tr.SayıYöntemleri
    with kojo.tr.HarfYöntemleri
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
    with kojo.tr.KuyrukYöntemleri
    with kojo.tr.YazıYöntemleri

class TurkishQueuesStringsTest extends AnyFunSuite with Matchers {
  import TurkishQueuesStrings._

  test("yazı: the Turkish names of String") {
    "merhaba".boyu should be(7)
    "merhaba".başı should be('m')
    "merhaba".sonu should be('a')
    "merhaba".tersi should be("abahrem")
    "merhaba".büyükHarfe should be("MERHABA")
    "MERHABA".küçükHarfe should be("merhaba")
    "  bosluk  ".kısalt should be("bosluk")
    "a,b,c".böl(',') should be(List("a", "b", "c"))
    "merhaba".al(3) should be("mer")
    "merhaba".düşür(3) should be("haba")
    "merhaba".içeriyorMu("rha") should be(doğru)
    "merhaba".sırası("h") should be(3)
    "aabbcc".yinelemesiz should be("abc")
    "aabbcc".yinelemesizİşlevle(identity) should be("abc")
    "merhaba".ele(_ != 'a') should be("merhb")
    "abc".düzİşle(h => h.toString * 2) should be("aabbcc")
    "abc".say(_ > 'a') should be(2)
    "3.5".kesire should be(3.5)
    "42".sayıya should be(42)
    "42".sayıyaBelki should be(Some(42))
    "kırk iki".sayıyaBelki should be(None)
    "3.5".kesireBelki should be(Some(3.5))
    Yazı.olarak(doğru) should be("doğru")
    Yazı.olarak(42) should be("42")
  }

  test("the Turkish rule for the dotted and dotless i") {
    'i'.büyükHarfe should be('İ')
    'ı'.büyükHarfe should be('I')
    'I'.küçükHarfe should be('ı')
    'İ'.küçükHarfe should be('i')
    'a'.büyükHarfe should be('A')
    'Z'.küçükHarfe should be('z')

    "istanbul".büyükHarfe should be("İSTANBUL")
    "ışık".büyükHarfe should be("IŞIK")
    "IRMAK".küçükHarfe should be("ırmak")
    "İZMİR".küçükHarfe should be("izmir")
    "izmir".ilkHarfiBüyült should be("İzmir")
  }

  test("the shared core: Dizi, Dizin, Yöney, Dizik, Küme, Eşlek, Kuyruk, Yazı and Belki") {
    val d = Seq(3, 1, 2)
    d.başıBelki should be(Some(3)); d.bul(_ > 1) should be(Some(3)); d.bulSondan(_ > 1) should be(Some(2))
    d.nerede(_ == 1) should be(1); d.neredeSondan(_ > 1) should be(2)
    d.dilimSırası(Seq(1, 2)) should be(1); d.sıralar.toList should be(List(0, 1, 2))
    d.başındaMı(Seq(3, 1)) should be(doğru); d.sonundaMı(Seq(2)) should be(doğru)
    d.karşılıklıMı(Seq(6, 2, 4))(_ * 2 == _) should be(doğru)
    d.böl(_ > 1) should be((Seq(3, 2), Seq(1)))
    d.bölDoğruKaldıkça(_ > 2) should be((Seq(3), Seq(1, 2)))
    d.bölYerinden(1) should be((Seq(3), Seq(1, 2)))
    d.öbekli(2).toList should be(List(Seq(3, 1), Seq(2)))
    d.kayarÖbekli(2).toList should be(List(Seq(3, 1), Seq(1, 2)))
    d.öbekleİşleİndirge(_ % 2)(x => x)(_ + _) should be(Map(1 -> 4, 0 -> 2))
    d.kombinasyonlar(2).toList.boyu should be(3); d.permütasyonlar.size should be(6)
    d.kuyruklar.toList.boyu should be(4); d.önler.toList.boyu should be(4)
    d.katla(0)(_ + _) should be(6); d.indirgeBelki(_ + _) should be(Some(6))
    d.tara(0)(_ + _) should be(Seq(0, 3, 4, 6)); d.taraSağdan(0)(_ + _) should be(Seq(6, 3, 2, 0))
    d.enUfağıBelki should be(Some(1)); d.enİrisiBelki should be(Some(3))
    d.sonunaEkle(9) should be(Seq(3, 1, 2, 9)); d.önüneEkle(9) should be(Seq(9, 3, 1, 2))
    d.uzat(5, 0) should be(Seq(3, 1, 2, 0, 0)); d.yama(1, Seq(8), 1) should be(Seq(3, 8, 2))
    d.fark(Seq(1)) should be(Seq(3, 2)); d.kesişim(Seq(2, 3)) should be(Seq(3, 2))
    d.bileşim(Seq(4)) should be(Seq(3, 1, 2, 4))
    d.seçİşle { case x if x > 1 => x * 10 } should be(Seq(30, 20))
    d.seçİşleİlk { case x if x < 3 => x } should be(Some(1))
    Seq(Seq(1, 2), Seq(3)).düzleştir should be(Seq(1, 2, 3))
    Seq((1, "a"), (2, "b")).ikiliyiAç should be((Seq(1, 2), Seq("a", "b")))
    d.ikileHepsini(Seq("x"), -1, "-") should be(Seq((3, "x"), (1, "-"), (2, "-")))
    d.tersİşle(_ * 2) should be(Seq(4, 2, 6))

    List(3, 1, 2).böl(_ > 1) should be((List(3, 2), List(1)))
    Vector(3, 1, 2).tara(0)(_ + _) should be(Vector(0, 3, 4, 6))
    Vector(3, 1, 2).enİrisiBelki should be(Some(3))
    val dk = Dizik(3, 1, 2)
    dk.bul(_ > 1) should be(Some(3)); dk.böl(_ > 1)._1 should be(Dizik(3, 2))
    dk.taraSoldan(0)(_ + _) should be(Dizik(0, 3, 4, 6)); dk.sonunaEkle(9) should be(Dizik(3, 1, 2, 9))

    val k = Küme(3, 1, 2)
    k.bul(_ > 2) should be(Some(3)); k.katla(0)(_ + _) should be(6)
    k.enUfağıBelki should be(Some(1)); k.fark(Küme(1)) should be(Küme(3, 2))
    k.seçİşle { case x if x > 1 => x * 10 } should be(Küme(30, 20))

    val e = Eşlek("a" -> 1, "b" -> 2)
    e.başıBelki should be(Some(("a", 1))); e.bul(_._2 > 1) should be(Some(("b", 2)))
    e.böl(_._2 > 1)._1 should be(Eşlek("b" -> 2))
    e.öbekleİşleİndirge(_._2 % 2)(_._2)(_ + _) should be(Map(1 -> 1, 0 -> 2))
    e.seçİşle { case (a, v) if v > 1 => a }.toSet should be(Set("b"))

    val ku = Kuyruk(3, 1, 2)
    ku.bul(_ > 1) should be(Some(3)); ku.böl(_ > 1)._1 should be(Kuyruk(3, 2))
    ku.tara(0)(_ + _) should be(Kuyruk(0, 3, 4, 6)); ku.boyu should be(3)

    val y = "merhaba"
    y.başıBelki should be(Some('m')); y.nerede(_ == 'h') should be(3)
    y.bölYerinden(3) should be(("mer", "haba"))
    y.öbekli(3).toList should be(List("mer", "hab", "a"))
    "abc".kombinasyonlar(2).toList should be(List("ab", "ac", "bc"))
    "ab".uzat(4, '-') should be("ab--"); "mer".sonunaEkleHepsini("haba") should be("merhaba")

    val b: Belki[Sayı] = Some(5)
    b.seçİşle { case x if x > 1 => x * 10 } should be(Some(50))
    b.içeriyorMu(5) should be(doğru); b.varMı(_ > 1) should be(doğru)
    b.katla(0)(_ * 2) should be(10); (None: Belki[Sayı]).katla(-1)(_ * 2) should be(-1)
    Some(Some(7)).düzleştir should be(Some(7)); b.ikile(Some("a")) should be(Some((5, "a")))
    Some((1, "a")).ikiliyiAç should be((Some(1), Some("a")))
  }

  test("names that belong to one type, and the ones that change in place") {
    "merhaba\ndünya".satırlar.toList should be(List("merhaba", "dünya"))
    "merhaba".başındanAt("mer") should be("haba"); "merhaba".sonundanAt("aba") should be("merh")
    "42".uzuna should be(42L); "abc".uzunaBelki should be(None)
    "merhaba".ikiyeAyır(_ == 'a') should be(("aa", "merhb"))
    "abc".seçİşle { case h if h != 'b' => h.büyükHarfe } should be(Seq('A', 'C'))

    val k = Küme(1, 2)
    k.ekli(3) should be(Küme(1, 2, 3)); k.çıkarılmış(1) should be(Küme(2))
    Küme(1).altKümesiMi(k) should be(doğru)

    val b: Belki[Sayı] = Some(5)
    b.boşsaÖbürü(Some(9)) should be(Some(5))
    (None: Belki[Sayı]).boşsaÖbürü(Some(9)) should be(Some(9))
    b.sola("sağdaki") should be(Left(5)); b.sağa("soldaki") should be(Right(5))
    Belki.iseVer(doğru)(3) should be(Some(3)); Belki.değilseVer(doğru)(3) should be(None)

    val ku = Kuyruk(1, 2)
    ku.kuyruğaEkle(3) should be(Kuyruk(1, 2, 3))
    ku.ilki should be(1); ku.baştanÇıkar() should be(1); ku should be(Kuyruk(2, 3))
    ku.baştanÇıkarBelki should be(Some(2)); ku.sondanÇıkar() should be(3)
    ku.kuyruğaEkleHepsini(Seq(7, 8)); ku should be(Kuyruk(7, 8))
    ku.eleYerinde(_ > 7); ku should be(Kuyruk(8))
    ku.boşalt(); ku.boşMu should be(doğru)

    val m = Eşlem("a" -> 1)
    m.koy("b", 2) should be(None); m.boyu should be(2)
    m.güncelle("a", 10); m.al("a") should be(Some(10))
    m.alYoksaEkle("c", 3) should be(3); m.alYoksaEkle("c", 9) should be(3)
    m.çıkar("c") should be(Some(3))
    m.değerleriİşleYerinde((_, d) => d * 2); m.al("a") should be(Some(20))
    m.eleYerinde(_._1 == "a"); m.boyu should be(1)
    var toplam = 0; m.herİkiliİçin((_, d) => toplam += d); toplam should be(20)
    m.anahtarYineleyici.toList should be(List("a"))
    m.boşalt(); m.boşMu should be(doğru)

    val ek = Eşlek("a" -> 1, "b" -> 2)
    ek.çıkarılmış("a") should be(Eşlek("b" -> 2)); ek.boyu should be(2)
    ek.değiştirİşlevle("a")(_ => Some(9)) should be(Eşlek("a" -> 9, "b" -> 2))
    ek.değerleriİşle(_ * 10) should be(Eşlek("a" -> 10, "b" -> 20))
    ek.dönüştür((_, d) => d + 1) should be(Eşlek("a" -> 2, "b" -> 3))

    val ö = ÖncelikSırası(3, 1, 2)
    ö.başıBelki should be(Some(3)); ö.bul(_ < 2) should be(Some(1))
    ö.böl(_ > 1)._1.sıralı should be(Seq(2, 3))
    ö.katla(0)(_ + _) should be(6); ö.taraSoldan(0)(_ + _) should be(Seq(0, 3, 4, 6))
    ö.seçİşle { case x if x > 1 => x * 10 }.sıralı should be(Seq(20, 30))
    ö.kuyruğa.boyu should be(3)
    ö.işleYerinde(_ * 10); ö.başı should be(30)
  }

  test("the Aralık type alias and the EsnekYazı buffer") {
    val a: Aralık = Aralık(1, 10, 3)
    a.ilki should be(1); a.sonuncu should be(10); a.adım should be(3); a.uzunluğu should be(3)
    a.başı should be(1); a.sonu should be(7); a.dizine should be(List(1, 4, 7))
    a.yazı() should be("Aralık(1, 4, 7)")
    a.içindeMi(4) should be(doğru)
    a.bul(_ > 3) should be(Some(4))
    a.böl(_ > 3)._1 should be(Seq(4, 7))
    a.enİrisiBelki should be(Some(7))
    (1 |-| 10) should be(Aralık.kapalı(1, 10))

    val ey = new EsnekYazı("merhaba")
    ey.bul(_ == 'h') should be(Some('h'))
    ey.harf(0) should be('m'); ey.parçası(0, 3) should be("mer")
    ey.ekleAraya(0, "Ey "); ey.yazıya should be("Ey merhaba")
    (new EsnekYazı("dünya")).araEkle(0, "merhaba ").yazıya should be("merhaba dünya")
    ey.aralığıSil(0, 3); ey.yazıya should be("merhaba")
    ey.harfiSil(0); ey.yazıya should be("erhaba")
    ey.harfiKur(0, 'M'); ey.yazıya should be("Mrhaba")
    ey.değiştirAralığını(0, 1, "me"); ey.yazıya should be("merhaba")
    ey.ekleHepsini(Seq('!', '!')); ey.yazıya should be("merhaba!!")
    ey.boyuKur(7); ey.tersiYerinde.yazıya should be("abahrem")
    new EsnekYazı("42").uzuna should be(42L)
    ey.sil(); ey.boşMu should be(doğru)
  }

  test("stack / queue / priority queue") {
    val y = Yığın.boş[Sayı]
    y.it(1); y.it(2)
    y.tepesi should be(2); y.tepe should be(2)
    y.çek() should be(2)
    y.boyu should be(1)
    y.koyHepsini(Seq(7, 8)); y.tepe should be(8); y.tane should be(3)
    y.dizi.başı should be(8)
    y.bul(_ > 7) should be(Some(8))
    y.böl(_ > 6)._1 should be(Seq(8, 7))
    y.enİrisiBelki should be(Some(8))
    Yığın.doldur(y).dizine should be(y.dizine)
    Yığın(1, 2, 3).tepe should be(3)
    Yığın(1, 2, 3).dizine should be(List(3, 2, 1))

    val k = Kuyruk.boş[Sayı]
    k.ekle(1); k.ekle(2)
    k.başı should be(1)
    k.çıkar() should be(1)
    k.boyu should be(1)

    val ö = ÖncelikSırası(3, 1, 2)
    ö.çıkar() should be(3)
    ö.boyu should be(2)
  }

  test("koy: the shared name in Yığın, Kuyruk and ÖncelikSırası") {
    val y = Yığın.boş[Sayı]
    y.koy(1); y.koy(2)
    y.tepe should be(2)

    val k = Kuyruk.boş[Sayı]
    k.koy(1); k.koy(2)
    k.başı should be(1)          // on a Kuyruk koy = ekle = enqueue
    k.koyHepsini(Seq(3, 4)); k.boyu should be(4)
    k.baştanAl() should be(1)

    val ö = ÖncelikKuyruğu.boş[Sayı]
    ö.koy(1); ö.koy(3); ö.koy(2)
    ö.başı should be(3)
    ö.koyHepsini(Seq(9)); ö.başı should be(9)

    // al(n) is take(n): it gives a copy and does not shrink the queue
    val k2 = Kuyruk(1, 2, 3)
    k2.al(2) should be(Kuyruk(1, 2))
    k2.boyu should be(3)
  }

  test("leak: the deque methods leak into Yığın and Kuyruk too (pinned)") {
    val ad = classOf[scala.collection.mutable.ArrayDeque[_]]
    ad.isAssignableFrom(classOf[scala.collection.mutable.Stack[_]]) should be(true)
    ad.isAssignableFrom(classOf[scala.collection.mutable.Queue[_]]) should be(true)

    val y = Yığın.boş[Sayı]
    y.koy(1); y.koy(2)
    y.tepe should be(2)
    y.başaKoy(0)
    y.toList should be(List(0, 2, 1))
    y.sondanAl() should be(1)

    // Kuyruk: it breaks FIFO
    val k = Kuyruk.boş[Sayı]
    k.koy(1); k.koy(2)
    k.başaKoy(0)
    k.başı should be(0)
    k.sondanAl() should be(2)
  }

  test("İkiUçluKuyruk: you can put at and take from both ends") {
    val d = İkiUçluKuyruk.boş[Sayı]
    d.koy(2); d.koy(3)           // at the end
    d.başaKoy(1)
    d.dizi should be(List(1, 2, 3))
    d.başı should be(1); d.sonu should be(3)
    d.baştanAl() should be(1)
    d.sondanAl() should be(3)
    d.dizi should be(List(2))
    d.boyu should be(1); d.doluMu should be(true)

    d.baştanAl() should be(2)
    d.boşMu should be(true)
    d.baştanAlBelki should be(None)
    d.sondanAlBelki should be(None)
    d.başıBelki should be(None)

    İkiUçluKuyruk(1, 2, 3).dizi should be(List(1, 2, 3))
    İkiUçluKuyruk(1, 2, 3).tersi.dizi should be(List(3, 2, 1))
    İkiUçluKuyruk(1, 2).ikizle().koy(3).boyu should be(3)
    val e = İkiUçluKuyruk(1, 2); e.ekleHepsini(Seq(3, 4)); e.dizi should be(List(1, 2, 3, 4))
    e.sil(); e.boşMu should be(true)
  }

  test("ÖncelikSırası: the core methods that come from the desktop") {
    val ö = ÖncelikSırası(3, 1, 4, 1, 5)
    ö.başı shouldBe 5
    ö.boyu shouldBe 5
    ö.varMı(_ == 4) shouldBe true
    ö.hepsiDoğruMu(_ > 0) shouldBe true
    ö.say(_ > 2) shouldBe 3
    ö.topla shouldBe 14
    ö.çarp shouldBe 60
    ö.enİrisi shouldBe 5
    ö.enUfağı shouldBe 1
    ö.indirge(_ + _) shouldBe 14
    ö.soldanKatla(0)(_ + _) shouldBe 14
    ö.sağdanKatla(0)(_ + _) shouldBe 14
    ö.ele(_ > 2).boyu shouldBe 3
    ö.eleDeğilse(_ > 2).boyu shouldBe 2
    ö.işle(_ * 2).toSeq.sorted shouldBe Seq(2, 2, 6, 8, 10)
    ö.dizine.sorted shouldBe List(1, 1, 3, 4, 5)
    ö.diziye.sorted shouldBe Seq(1, 1, 3, 4, 5)
    ö.kümeye shouldBe Set(1, 3, 4, 5)
    ö.yöneye.sorted shouldBe Vector(1, 1, 3, 4, 5)
    ö.dizime.diziye.sorted shouldBe Seq(1, 1, 3, 4, 5)
    ö.öbekle(_ % 2).keySet shouldBe Set(0, 1)
    ö.ikileSırayla.size shouldBe 5
    val k = ö.ikizle()
    k.baştanAl() shouldBe 5
    k.baştanAl() shouldBe 4
    ö.boyu shouldBe 5
    ö.ikizle().baştanAlHepsini shouldBe Seq(5, 4, 3, 1, 1)
    val ö2 = ÖncelikSırası.boş[Sayı]
    ö2.ekle(2, 7, 4)
    ö2.başı shouldBe 7
    val ö3 = ÖncelikSırası((1, "bir"), (2, "iki"))
    ö3.eşleğe shouldBe Map(1 -> "bir", 2 -> "iki")
    ö3.eşleme.al(2) shouldBe Some("iki")
  }

  test("Yığın: taking in bulk, changing in place, access by position") {
    val y = Yığın(1, 2, 3) // push 1, push 2, push 3 -> 3 on the top
    y(0) shouldBe 3
    Yığın(1, 2, 3).çekHepsini shouldBe Seq(3, 2, 1)       // from the top to the bottom
    Yığın(1, 2, 3).alHepsini shouldBe Seq(3, 2, 1)
    Yığın(1, 2, 3).alHepsiniTersten shouldBe Seq(1, 2, 3) // from the bottom to the top
    Yığın(1, 2, 3).çekDoğruKaldıkça(_ > 1) shouldBe Seq(3, 2)
    Yığın(1, 2, 3).çekBelki shouldBe Some(3)
    Yığın.boş[Sayı].çekBelki shouldBe None
    val d = Yığın(1, 2, 3)
    d.çıkarSondan() shouldBe 1
    d.dizi shouldBe Seq(3, 2)
    Yığın(1, 2, 3).sondanÇıkarBelki shouldBe Some(1)

    Yığın(1, 2, 3, 4).eleYerinde(_ % 2 == 0).dizi shouldBe Seq(4, 2)
    Yığın(1, 2, 3).işleYerinde(_ * 10).dizi shouldBe Seq(30, 20, 10)
    Yığın(3, 1, 2).sıralıYerinde.dizi shouldBe Seq(1, 2, 3)
    Yığın(3, 1, 2).sıralaYerinde(-_).dizi shouldBe Seq(3, 2, 1)
    Yığın(3, 1, 2).sırayaSokYerinde(_ > _).dizi shouldBe Seq(3, 2, 1)
    Yığın(1, 2, 3, 4).alYerinde(2).dizi shouldBe Seq(4, 3)
    Yığın(1, 2, 3, 4).düşürYerinde(2).dizi shouldBe Seq(2, 1)
    Yığın(1, 2, 3, 4).alSağdanYerinde(2).dizi shouldBe Seq(2, 1)
    Yığın(1, 2, 3, 4).düşürSağdanYerinde(2).dizi shouldBe Seq(4, 3)
    Yığın(1, 2).uzatYerinde(4, 0).dizi shouldBe Seq(2, 1, 0, 0)
    Yığın(1, 2, 3, 4).dilimYerinde(1, 3).dizi shouldBe Seq(3, 2)

    val k = Yığın(1, 2, 3)
    k.güncelle(0, 9); k.tepe shouldBe 9
    k.ekleAraya(1, 7); k.dizi shouldBe Seq(9, 7, 2, 1)
    k.ekleArayaHepsini(0, Dizi(8, 8)); k.dizi shouldBe Seq(8, 8, 9, 7, 2, 1)
    k.çıkar(0) shouldBe 8
    Yığın(1, 2, 3).çıkarİlkUyanı(_ < 3) shouldBe Some(2)
    Yığın(1, 2, 3).dizime.diziye shouldBe Seq(3, 2, 1)
  }

  test("Kuyruk: the in-place methods of ArrayDeque") {
    Kuyruk(1, 2, 3, 4).alYerinde(2).dizine shouldBe List(1, 2)
    Kuyruk(1, 2, 3, 4).düşürYerinde(2).dizine shouldBe List(3, 4)
    Kuyruk(1, 2, 3, 4).alSağdanYerinde(2).dizine shouldBe List(3, 4)
    Kuyruk(1, 2, 3, 4).düşürSağdanYerinde(2).dizine shouldBe List(1, 2)
    Kuyruk(1, 2, 3, 4).alDoğruKaldıkçaYerinde(_ < 3).dizine shouldBe List(1, 2)
    Kuyruk(1, 2, 3, 4).düşürDoğruKaldıkçaYerinde(_ < 3).dizine shouldBe List(3, 4)
    Kuyruk(1, 2, 3, 4).dilimYerinde(1, 3).dizine shouldBe List(2, 3)
    Kuyruk(1, 2).uzatYerinde(4, 0).dizine shouldBe List(1, 2, 0, 0)
    Kuyruk(1, 2, 3).yamaYerinde(1, Dizi(8, 9), 1).dizine shouldBe List(1, 8, 9, 3)
    Kuyruk(1, 2).düzİşleYerinde(x => Dizi(x, x)).dizine shouldBe List(1, 1, 2, 2)
    Kuyruk(3, 1, 2).sırayaSokYerinde(_ > _).dizine shouldBe List(3, 2, 1)

    val k = Kuyruk(1, 2, 3)
    k.güncelle(0, 9); k.başı shouldBe 9
    k.ekleAraya(1, 7); k.dizine shouldBe List(9, 7, 2, 3)
    k.ekleArayaHepsini(0, Dizi(0, 0)); k.dizine shouldBe List(0, 0, 9, 7, 2, 3)
    k.ekleHepsini(Dizi(5)); k.sonu shouldBe 5

    Kuyruk(1, 2, 3).alHepsini shouldBe Seq(1, 2, 3)
    Kuyruk(1, 2, 3).alHepsiniTersten shouldBe Seq(3, 2, 1)
    Kuyruk(1, 2, 3).çıkarİlkUyanı(_ > 1) shouldBe Some(2)
    Kuyruk(1, 2, 3).çıkarSondan() shouldBe 3
    Kuyruk(1, 2, 3).çıkarSondanBelki shouldBe Some(3)
    Kuyruk(1, 2, 3).çıkarSondanDoğruKaldıkça(_ > 1) shouldBe Seq(3, 2)
    Kuyruk(2, 1, 2).baştanÇıkarHepsiniKoşulla(_ == 2) shouldBe Seq(2)
    Kuyruk(2, 1, 2).baştanAlHepsini(_ == 2) shouldBe Seq(2, 2)
  }

  test("dizime/eşleme: the two systematic names of the desktop work on every type") {
    Dizi(1, 2, 3).dizime.boyu should be(3)
    Dizin(1, 2).dizime.diziye should be(Dizi(1, 2))
    Yöney(1, 2).dizime.boyu should be(2)
    Aralık(1, 4).dizime.diziye should be(Dizi(1, 2, 3))
    MiskinDizin(1, 2).dizime.boyu should be(2)
    Küme(1, 2).dizime.boyu should be(2)
    Dizik(1, 2).dizime.boyu should be(2)
    "ab".dizime.boyu should be(2)
    Kuyruk(1, 2).dizime.boyu should be(2)
    Yığın(1, 2).dizime.boyu should be(2)

    val ikili = Dizi(("a", 1), ("b", 2))
    ikili.eşleme.boyu should be(2)
    ikili.eşleme.al("a") should be(Biri(1))
    Dizin(("a", 1)).eşleme.boyu should be(1)
    Yöney(("a", 1)).eşleme.boyu should be(1)
    Küme(("a", 1)).eşleme.boyu should be(1)
    Dizik(("a", 1)).eşleme.boyu should be(1)
    MiskinDizin(("a", 1)).eşleme.boyu should be(1)
    Kuyruk(("a", 1)).eşleme.boyu should be(1)
    Yığın(("a", 1)).eşleme.boyu should be(1)
  }

  test("yazı: comparing without regard to case") {
    "Kojo".eşitMiKüçükHarfBüyükHarfAyrımıYapmadan("kOJO") shouldBe true
    "a".kıyaslaKüçükHarfBüyükHarfAyrımıYapmadan("B") should be < 0
  }
}
