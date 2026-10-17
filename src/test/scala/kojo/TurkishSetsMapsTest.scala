package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishSetsMaps
    extends kojo.tr.SayıYöntemleri
    with kojo.tr.DizinYöntemleri
    with kojo.tr.YöneyYöntemleri
    with kojo.tr.DizimYöntemleri
    with kojo.tr.YinelenebilirYöntemleri
    with kojo.tr.EşlemYöntemleri
    with kojo.tr.KümeYöntemleri

class TurkishSetsMapsTest extends AnyFunSuite with Matchers {
  import TurkishSetsMaps._

  test("küme: the Turkish names of Set") {
    val k = Küme(1, 2, 3)
    k.boyu should be(3)
    k.içeriyorMu(2) should be(doğru)
    k.doluMu should be(doğru)
    Küme.boş[Sayı].boşMu should be(doğru)
    k.işle(_ * 2) should be(Set(2, 4, 6))
    k.ele(_ > 1) should be(Set(2, 3))
    k.indirge(_ + _) should be(6)
    k.topla should be(6)
    k.enİrisi should be(3)
    k.enUfağı should be(1)
    k.kesişim(Küme(2, 3, 4)) should be(Set(2, 3))
    k.bileşim(Küme(4)) should be(Set(1, 2, 3, 4))
    k.fark(Küme(1)) should be(Set(2, 3))
    k.böl(_ > 1) should be((Set(2, 3), Set(1)))
    k.dizine.sorted should be(List(1, 2, 3))
    k.altKümeleri(2).size should be(3)
  }

  test("eşlek / eşlem: the Turkish names of Map") {
    val e = Eşlek("a" -> 1, "b" -> 2)
    e.boyu should be(2)
    e.al("a") should be(Some(1))
    e.al("z") should be(None)
    e.alYoksa("z", 0) should be(0)
    e.eşli("b") should be(doğru)
    e.içeriyorMu("z") should be(yanlış)
    e.anahtarKümesi should be(Set("a", "b"))
    e.değerler.toList.sorted should be(List(1, 2))
    e.ele(_._2 > 1) should be(Map("b" -> 2))
    e.değiştirilmiş("a", 9).al("a") should be(Some(9))
    e.say(_._2 > 0) should be(2)
    e.enİrisiİşlevle(_._2) should be(("b", 2))
    Eşlek.boş[Yazı, Sayı].boşMu should be(doğru)

    // the mutable map
    val m = Eşlem("x" -> 1)
    m.sayı should be(1)
    m += ("y" -> 2)
    m.sayı should be(2)
    m.al("y") should be(Some(2))
    m -= "x"
    m.içeriyorMu("x") should be(yanlış)
    m("y") should be(2)
    Eşlem.boş[Yazı, Sayı].boşMu should be(doğru)
    Eşlem.değişmezden(Map("k" -> 9)).al("k") should be(Some(9))
  }

  test("Eşlem: the core methods that come from the desktop") {
    val e = Eşlem("a" -> 1, "b" -> 2, "c" -> 3)
    e.kaldır("a") shouldBe Some(1)
    e.kaldır("yok") shouldBe None
    e.sayı shouldBe 3
    e.hepsiİçinDoğruMu(_._2 > 0) shouldBe true
    e.alSırayla(2).size shouldBe 2
    e.düşür(2).size shouldBe 1
    e.alSağdan(1).size shouldBe 1
    e.düşürSağdan(1).size shouldBe 2
    e.kümeye.size shouldBe 3
    e.yöneye.size shouldBe 3
    e.dizime.boyu shouldBe 3
    e.ikileSırayla.size shouldBe 3
    e.ikile(Dizin(9, 8, 7)).size shouldBe 3
    e.işle((ikili: (Yazı, Sayı)) => ikili._2).toSeq.sorted shouldBe Seq(1, 2, 3)
    e.düzİşle(ikili => collection.mutable.Iterable(ikili._2)).toSeq.sorted shouldBe Seq(1, 2, 3)
    e.indirgeSoldan[(Yazı, Sayı)]((x, y) => (x._1 + y._1, x._2 + y._2))._2 shouldBe 6
    e.indirgeSoldanBelki[(Yazı, Sayı)]((x, y) => (x._1, x._2 + y._2)).get._2 shouldBe 6
    e.indirgeSağdanBelki[(Yazı, Sayı)]((x, y) => (y._1, x._2 + y._2)).get._2 shouldBe 6
    e.katla(("", 0))((x, y) => (x._1, x._2 + y._2))._2 shouldBe 6
    e.enİrisi(_._2)._2 shouldBe 3
    e.enUfağı(_._2)._2 shouldBe 1
    e.yazıYap("{", ", ", "}").startsWith("{") shouldBe true
    // değiştir: returns a COPY and does not touch the original (the same as değiştirilmiş)
    e.değiştir("a", 9)("a") shouldBe 9
    e("a") shouldBe 1
    // the ends
    e.kuyruğu.size shouldBe 2
    e.önü.size shouldBe 2
    e.sonu should not be null
  }
}
