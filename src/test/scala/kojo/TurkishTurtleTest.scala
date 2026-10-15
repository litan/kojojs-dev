package kojo

import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

class TurkishTurtleTest extends AsyncFunSuite with Matchers {
  import kojo.syntax.Builtins
  implicit val kojoWorld: TestKojoWorld = new TestKojoWorld()
  val builtins = new Builtins()
  import builtins._
  import builtins.trTurtle._
  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def square = Picture {
    canlandırmaHızınıKur(0)
    yinele(4) {
      ileri(50)
      sağ()
    }
  }

  test("a picture made with Turkish commands holds the lines it draws") {
    val p = square
    p.draw()
    for {
      _ <- p.ready
    } yield {
      val b = p.bounds
      b.width should be(52.0 +- 0.01) // 50 plus the pen thickness
      b.height should be(52.0 +- 0.01)
    }
  }

  test("Turkish and English commands give the same picture") {
    val tr = square
    val en = Picture {
      turtle.setAnimationDelay(0)
      for (_ <- 1 to 4) {
        turtle.forward(50)
        turtle.right(90)
      }
    }
    tr.draw(); en.draw()
    for {
      _ <- tr.ready
      _ <- en.ready
    } yield {
      tr.bounds.width should be(en.bounds.width +- 0.01)
      tr.bounds.height should be(en.bounds.height +- 0.01)
    }
  }
}
