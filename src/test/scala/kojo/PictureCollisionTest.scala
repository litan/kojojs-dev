package kojo

import org.scalatest.AsyncFunSuite
import org.scalatest.Matchers

class PictureCollisionTest extends AsyncFunSuite with Matchers with RepeatCommands {

  import kojo.syntax.Builtins
  implicit val kojoWorld = new TestKojoWorld()
  val builtins = new Builtins()
  import builtins._
  import builtins.Color._

  val psize = 50.0
  val delta = 0.01

  def testBox0(n: Double) = PictureT { t =>
    import t._
    repeat(4) {
      forward(n)
      right
    }
  }

  def testBox = testBox0(psize)

  def testTriangle = PictureT { t =>
    import t._
    right()
    repeat(3) {
      forward(psize)
      left(120)
    }
  }

  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  test("box-box non collision") {
    val p1 = trans(-psize / 2 - delta, 0) -> testBox
    val p2 = trans(psize / 2 + delta, 0) -> testBox
    p1.draw()
    p2.draw()
    for {
      _ <- p1.ready
      _ <- p2.ready
    } yield { p2.collidesWith(p1) should be(false) }
  }

  test("box-box collision") {
    val p1 = trans(-psize / 2, 0) -> testBox
    val p2 = trans(psize / 2, 0) -> testBox
    p1.draw()
    p2.draw()
    for {
      _ <- p1.ready
      _ <- p2.ready
    } yield { p2.collidesWith(p1) should be(true) }
  }

  test("gpics-box collision") {
    val p1 = trans(-psize / 2, 0) -> testBox
    val p2 = trans(psize / 2, 0) -> testBox
    val p3 = trans(psize / 2 + psize, 0) -> testBox
    val gpics = GPics(p1, p2)
    gpics.draw()
    p3.draw()
    for {
      _ <- p3.ready
      _ <- gpics.ready
    } yield { gpics.collidesWith(p3) should be(true) }
  }

  test("gpics-box non collision") {
    val p1 = trans(-psize / 2, 0) -> testBox
    val p2 = trans(psize / 2, 0) -> testBox
    val p3 = trans(psize / 2 + psize + delta, 0) -> testBox
    val gpics = GPics(p1, p2)
    gpics.draw()
    p3.draw()
    for {
      _ <- p3.ready
      _ <- gpics.ready
    } yield { gpics.collidesWith(p3) should be(false) }
  }

  test("box-tri non collision") {
    val p1 = testBox
    val p2 = trans(0, -math.sin(60.toRadians) * psize) -> testTriangle
    p1.draw()
    p2.draw()
    for {
      _ <- p1.ready
      _ <- p2.ready
    } yield { p1.collidesWith(p2) should be(false) }
  }

  test("box-tri collision") {
    val p1 = testBox
    val p2 = trans(0, -math.sin(60.toRadians) * psize + 10 * delta) -> testTriangle
    p1.draw()
    p2.draw()
    for {
      _ <- p1.ready
      _ <- p2.ready
    } yield { p1.collidesWith(p2) should be(true) }
  }

  test("box with many boxes collision") {
    val p1 = fillColor(blue) -> testBox
    val p2 = fillColor(blue) * trans(psize, 0) -> testBox
    val p3 = fillColor(blue) * trans(2 * psize, 0) -> testBox
    val p4 = fillColor(blue) * trans(3 * psize / 2, psize / 2) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = Set(p1, p2, p3)
    others.size should be(3)

    for {
      _ <- p1.ready
      _ <- p2.ready
      _ <- p3.ready
      _ <- p4.ready
    } yield {
      val cols = p4.collisions(others)
      cols.size should be(2)
      cols.contains(p1) should be(false)
      cols.contains(p2) should be(true)
      cols.contains(p3) should be(true)
    }
  }

  test("box with many boxes - non collision") {
    val p1 = fillColor(blue) -> testBox
    val p2 = fillColor(blue) * trans(psize, 0) -> testBox
    val p3 = fillColor(blue) * trans(2 * psize, 0) -> testBox
    val p4 = fillColor(blue) * trans(3 * psize / 2, psize + delta) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = Set(p1, p2, p3)
    others.size should be(3)

    for {
      _ <- p1.ready
      _ <- p2.ready
      _ <- p3.ready
      _ <- p4.ready
    } yield {
      val cols = p4.collisions(others)
      cols.size should be(0)
      cols.contains(p1) should be(false)
      cols.contains(p2) should be(false)
      cols.contains(p3) should be(false)
    }
  }

  //  test("no self collisions") {
  //    val p1 = fillColor(blue) -> testBox
  //    val p2 = fillColor(blue) * trans(psize, 0) -> testBox
  //    val p3 = fillColor(blue) * trans(2 * psize, 0) -> testBox
  //    val p4 = fillColor(blue) * trans(3 * psize / 2, psize / 2) -> testBox
  //
  //    p1.draw()
  //    p2.draw()
  //    p3.draw()
  //    p4.draw()
  //
  //    val others = Set(p1, p2, p3, p4)
  //    others.size should be(4)
  //
  //    for {
  //      _ <- p1.ready
  //      _ <- p2.ready
  //      _ <- p3.ready
  //      _ <- p4.ready
  //    } yield {
  //      val cols = p4.collisions(others)
  //      cols.size should be(2)
  //      cols.contains(p4) should be(false)
  //    }
  //  }
}
