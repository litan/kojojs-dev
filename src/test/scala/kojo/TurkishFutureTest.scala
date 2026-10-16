package kojo

import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

object TurkishFuture extends kojo.tr.GelecekYöntemleri

class TurkishFutureTest extends AsyncFunSuite with Matchers {
  import TurkishFuture._
  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  test("Gelecek: işle, düzİşle and ele on the default (global) execution context") {
    for {
      a <- Gelecek.başarılı(2).işle(_ * 3)
      b <- Gelecek.başarılı(2).düzİşle(x => Gelecek.başarılı(x + 10))
      c <- Gelecek.başarılı(5).ele(_ > 1)
    } yield {
      a shouldBe 6
      b shouldBe 12
      c shouldBe 5
    }
  }

  test("Gelecek.başarısız gives a failed future") {
    Gelecek.başarısız[Sayı](new KuralDışı("hata")).failed.map(e => e.getMessage shouldBe "hata")
  }

  test("İşletimBağlamı.küresel is the global context") {
    (İşletimBağlamı.küresel eq scala.concurrent.ExecutionContext.global) shouldBe true
  }
}

/** A script that has an execution context of its own (here the `queue` that many samples import) must compile: no ambiguity. */
class TurkishFutureOwnContextTest extends org.scalatest.funsuite.AnyFunSuite with Matchers {
  import TurkishFuture._
  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  test("Gelecek: işle, düzİşle and ele use the context that the script has, without an ambiguity error") {
    val g: Gelecek[Sayı] = Gelecek.başarılı(1).işle(_ + 1).düzİşle(x => Gelecek.başarılı(x * 2)).ele(_ > 0)
    g.isInstanceOf[scala.concurrent.Future[_]] shouldBe true
  }
}
