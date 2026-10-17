package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishKeys extends kojo.tr.KlavyeYöntemleri

/** The values are DOM keyCodes, not AWT VK_* values: `gir` is 13 here (AWT: 10), `noktalıVirgül` 186 (AWT: 0x3b). */
class TurkishKeyboardTest extends AnyFunSuite with Matchers {
  import TurkishKeys._

  test("key names: the English aliases of the desktop are here too (camelCase), with the DOM values") {
    tuşlar.enter should be(tuşlar.gir)
    tuşlar.backSpace should be(tuşlar.silGeri)
    tuşlar.cancel should be(tuşlar.iptal)
    tuşlar.clear should be(tuşlar.temizle)
    tuşlar.shift should be(tuşlar.kaldırma)
    tuşlar.control should be(tuşlar.kontrol)
    tuşlar.pause should be(tuşlar.dur)
    tuşlar.escape should be(tuşlar.çık)
    tuşlar.pageUp should be(tuşlar.sayfaYukarı)
    tuşlar.pageDown should be(tuşlar.sayfaAşağı)
    tuşlar.end should be(tuşlar.satırSonu)
    tuşlar.home should be(tuşlar.satırBaşı)
    // the DOM values are kept (NOT the AWT ones)
    tuşlar.enter should be(13)
    tuşlar.backSpace should be(8)
    tuşlar.escape should be(0x1b)
  }

  // The deprecated spellings must still work: the desktop gives them, so scripts written there use them.
  // This test uses the deprecated names on purpose; the compiler warning is expected.
  test("key names: the deprecated snake_case spellings give the same key") {
    tuşlar.back_space should be(tuşlar.backSpace)
    tuşlar.page_up should be(tuşlar.pageUp)
    tuşlar.page_down should be(tuşlar.pageDown)
  }

  test("key names: ALL the DOM values are pinned (62 names)") {
    // control and editing
    tuşlar.gir should be(13)
    tuşlar.silGeri should be(8)
    tuşlar.sekme should be(9)
    tuşlar.iptal should be(0x03)
    tuşlar.temizle should be(0x0c)
    tuşlar.kaldırma should be(0x10)
    tuşlar.kontrol should be(0x11)
    tuşlar.alt should be(0x12)
    tuşlar.dur should be(0x13)
    tuşlar.büyükHarfKilidi should be(0x14)
    tuşlar.çık should be(0x1b)
    tuşlar.boşluk should be(0x20)
    tuşlar.sayfaYukarı should be(0x21)
    tuşlar.sayfaAşağı should be(0x22)
    tuşlar.satırSonu should be(0x23)
    tuşlar.satırBaşı should be(0x24)

    // the arrow keys -- the ones that the samples use most
    tuşlar.sol should be(0x25)
    tuşlar.yukarı should be(0x26)
    tuşlar.sağ should be(0x27)
    tuşlar.aşağı should be(0x28)

    // punctuation -- the set that differs most from the desktop
    tuşlar.virgül should be(188)
    tuşlar.eksi should be(189)
    tuşlar.nokta should be(190)
    tuşlar.bölü should be(191)
    tuşlar.noktalıVirgül should be(186)
    tuşlar.eşittir should be(187)

    // digits and letters are lined up with ASCII
    val rakamlar = List(
      tuşlar.n0, tuşlar.n1, tuşlar.n2, tuşlar.n3, tuşlar.n4,
      tuşlar.n5, tuşlar.n6, tuşlar.n7, tuşlar.n8, tuşlar.n9
    )
    rakamlar should have size 10
    rakamlar.zipWithIndex.foreach { case (değer, i) => değer should be(0x30 + i) }

    val harfler = List(
      tuşlar.a, tuşlar.b, tuşlar.c, tuşlar.d, tuşlar.e, tuşlar.f, tuşlar.g,
      tuşlar.h, tuşlar.i, tuşlar.j, tuşlar.k, tuşlar.l, tuşlar.m, tuşlar.n,
      tuşlar.o, tuşlar.p, tuşlar.q, tuşlar.r, tuşlar.s, tuşlar.t, tuşlar.u,
      tuşlar.v, tuşlar.w, tuşlar.x, tuşlar.y, tuşlar.z
    )
    harfler should have size 26
    harfler.zipWithIndex.foreach { case (değer, i) => değer should be(0x41 + i) }
  }

  test("key names: the aliases are not cut off from their roots") {
    tuşlar.backSpace should be(tuşlar.silGeri)
    tuşlar.pageUp should be(tuşlar.sayfaYukarı)
    tuşlar.pageDown should be(tuşlar.sayfaAşağı)
    tuşlar.kaç should be(tuşlar.çık)
    tuşlar.ev should be(tuşlar.satırBaşı)
  }

  test("tuşBasılıMı, canlandır and canlandırmayıDurdur work through trTurtle") {
    implicit val kojoWorld: TestKojoWorld = new TestKojoWorld()
    val builtins = new kojo.syntax.Builtins()
    import builtins.trTurtle._
    tuşBasılıMı(tuşlar.sol) should be(false)
    canlandır { () }
    canlandırmayıDurdur()
    tuşlar.sol should be(0x25)
  }
}
