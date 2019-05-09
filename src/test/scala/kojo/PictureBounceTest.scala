package kojo

import org.scalatest.AsyncFunSuite
import org.scalatest.Matchers

class PictureBounceTest extends AsyncFunSuite with Matchers {
  import kojo.syntax.Builtins
  implicit val kojoWorld = new TestKojoWorld()
  val builtins = new Builtins()
  import builtins._
  import builtins.Color._
  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  val rg = new java.util.Random

  def picline(x1: Double, y1: Double, x2: Double, y2: Double) = PictureT { t =>
    import t._
    setPosition(x1, y1)
    moveTo(x2, y2)
  }

  def stageCorner = PictureT { t =>
    import t._
    right(90)
    forward(200)
    left(90)
    forward(200)
  }

  def stageCorner2 = PictureT { t =>
    import t._
    forward(200)
    right(90)
    forward(200)
  }

  test("bounce 1") {
    val obj = picline(0, 0, 100, 0)
    val pic = trans(50, -20) -> Picture.circle(20)
    obj.draw(); pic.draw()
    val v = Vector2D(0, 100)
    for {
      _ <- obj.ready
      _ <- pic.ready
    } yield {
      bouncePicVectorOffPic(pic, v, obj) shouldBe Vector2D(0, -100)
    }
  }

  test("bounce ball off stage corner") {
    val obj = stageCorner
    val ball = trans(182, 18) -> Picture.circle(20)
    obj.draw(); ball.draw()
    val v = Vector2D(100, -100)
    for {
      _ <- obj.ready
      _ <- ball.ready
    } yield {
      bouncePicVectorOffPic(ball, v, obj) shouldBe Vector2D(-100, 100)
    }
  }

  test("bounce ball off stage corner2") {
    val obj = stageCorner2
    val ball = trans(18, 182) -> Picture.circle(20)
    obj.draw(); ball.draw();
    val v = Vector2D(-100, 100)
    for {
      _ <- obj.ready
      _ <- ball.ready
    } yield {
      bouncePicVectorOffPic(ball, v, obj) shouldBe Vector2D(100, -100)
    }
  }

  test("bounce rectangle off circle") {
    val d = math.sin(45.toRadians) * (40 + math.sqrt(400 + 400))
    val obj = trans(d, d) -> Picture.circle(40)
    val rec = trans(2, 2) -> Picture.rect(20, 20)
    obj.draw(); rec.draw()
    val v = Vector2D(50, 50)
    for {
      _ <- obj.ready
      _ <- rec.ready
    } yield {
      bouncePicVectorOffPic(rec, v, obj) shouldBe Vector2D(-50, -50)
    }
  }

//  test("bounce rectangle off head-on line") {
//    val obj = picline(0, 20, 0, 100)
//    val rec = trans(-10, 0) -> Picture.rect(20, 20)
//    obj.draw(); rec.draw()
//    val v = Vector2D(0, 50)
//    for {
//      _ <- obj.ready
//      _ <- rec.ready
//    } yield {
//      bouncePicVectorOffPic(rec, v, obj) shouldBe Vector2D(0, -50)
//    }
//  }
}