package kojo

import java.util.concurrent.CountDownLatch

import org.scalatest.AsyncFunSuite
import org.scalatest.Matchers

import Utils.doublesEqual

class PictureTest extends AsyncFunSuite with Matchers with RepeatCommands {

  import kojo.syntax.Builtins
  implicit val kojoWorld = new TestKojoWorld()
  val builtins = new Builtins()
  import builtins._
  import builtins.Color._
  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


  val psize = 50
  val pt = 2.0
  val w = psize + pt
  val h = psize + pt
  val bx = -pt / 2
  val by = -pt / 2
  val w2 = w * 2
  val bx2 = psize * 2

  def testPic = PictureT { t =>
    import t._
    invisible()
    setAnimationDelay(0)
    repeat(4) {
      forward(psize)
      right
    }
  }

  def testLine = PictureT { t =>
    import t._
    right()
    forward(100)
  }

  test("picture bounds") {
    val p = testPic
    p.draw()
    for {
      _ <- p.ready
    } yield {
      val b = p.bounds
      b.x should be(bx +- 0.01)
      b.y should be(by +- 0.01)
      b.width should be(w +- 0.01)
      b.height should be(h +- 0.01)
    }
  }

  test("picture translation") {
    val p = trans(50, 0) -> testPic
    p.draw()

    for {
      _ <- p.ready
    } yield {
      val b = p.bounds
      b.x should be(50 + bx +- 0.01)
    }
  }

  test("picture scaling") {
    val p = scale(2, 2) -> testPic
    p.draw()
    for {
      _ <- p.ready
    } yield {
      val b = p.bounds
      b.x should be(bx * 2 +- 0.01)
      b.width should be(w * 2 +- 0.01)
      b.height should be(h * 2 +- 0.01)
    }
  }

  test("picture scaling after translation") {
    val p = trans(50, 0) * scale(2, 2) -> testPic
    p.draw()
    for {
      _ <- p.ready
    } yield {
      val b = p.bounds
      b.x should be(50 + 2 * bx +- 0.01)
      b.width should be(w * 2 +- 0.01)
      b.height should be(h * 2 +- 0.01)
    }
  }

  test("picture translation after scaling") {
    val p = scale(2, 2) * trans(50, 0) -> testPic
    p.draw()
    for {
      _ <- p.ready
    } yield {
      val b = p.bounds
      b.x should be(50 * 2 + 2 * bx +- 0.01)
      b.width should be(w * 2 +- 0.01)
      b.height should be(h * 2 +- 0.01)
    }
  }

  test("heading 1") {
    val pic = testLine
    pic.rotate(390)
    pic.heading should be(30.0 +- 0.001)
  }

  test("heading 2") {
    val pic = testLine
    pic.setHeading(2 * 360 + 20)
    pic.heading should be(20.0 +- 0.001)
  }

  test("position 1") {
    val pic = testLine
    pic.translate(100, 50)
    pic.position.x should be(100)
    pic.position.y should be(50)
  }

  test("position + heading") {
    val pic = testLine
    pic.rotate(30)
    pic.translate(100, 0)
    pic.position.x should be(100 * math.cos(30.toRadians) +- 0.001)
    pic.position.y should be(100 * math.sin(30.toRadians) +- 0.001)

    pic.setPosition(150, 50)
    pic.position.x should be(150)
    pic.position.y should be(50)
    pic.heading should be(30.0 +- 0.001)
  }

  //  test("react provides correct me for transforms") {
  //    val pic = rot(30) -> testPic
  //    @volatile var pic2: core.Picture = null
  //    pic.draw()
  //    val latch = new CountDownLatch(1)
  //    pic.react { me =>
  //      pic2 = me
  //      pic.canvas.stopAnimation()
  //      latch.countDown()
  //    }
  //    latch.await()
  //    pic2 should be(pic)
  //  }
}
