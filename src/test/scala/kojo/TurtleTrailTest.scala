package kojo

import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers
import pixiscalajs.PIXI

import scala.concurrent.Promise

/**
 * A test world whose `render` really renders. The turtle renders after every step it takes; in the plain
 * `TestKojoWorld` that is a no-op, so a drawing that is lost between renders cannot show.
 */
class RenderingTestKojoWorld extends TestKojoWorld {
  private lazy val renderer = PIXI.Pixi.autoDetectRenderer(scala.scalajs.js.Dynamic.literal(width = canvasWidth, height = canvasHeight))
  override def render(): Unit = renderer.render(stage)
}

/**
 * PIXI 5 finishes the path of a `Graphics` at every render, and a path of two points is dropped: a turtle
 * that draws `moveTo`, render, `lineTo`, render left no trail at all. The turtle goes on from its last
 * point after a render (`PixiCompat.continuePath`). Under PIXI 4 these hold as well.
 */
class TurtleTrailTest extends AsyncFunSuite with Matchers {
  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  implicit val kojoWorld: RenderingTestKojoWorld = new RenderingTestKojoWorld()

  private def run(commands: Turtle => Unit): scala.concurrent.Future[Turtle] = {
    val t = new Turtle(0, 0)
    val done = Promise[Turtle]()
    t.setAnimationDelay(20)
    commands(t)
    t.sync(() => done.success(t))
    done.future
  }

  // The test stage has its origin at (400, 300) and y up, so a turtle at (x, y) is at (400 + x, 300 - y) in global
  // coordinates. The turtle starts away from the origin: a line that PIXI 5 starts again from (0, 0) would
  // make the bounds reach the origin.
  private val ox = 400.0
  private val oy = 300.0

  test("the line of an animated turtle stays where it was drawn, after the renders between its steps") {
    run { t =>
      t.setPosition(200, 200)
      t.forward(50)
      t.right()
      t.forward(50)
    }.map { t =>
      val b = t.turtlePath.getBounds()
      b.x should be(ox + 200 - 1.0 +- 1.0)
      b.y should be(oy - 250 - 1.0 +- 1.0)
      b.width should be(52.0 +- 1.0)
      b.height should be(52.0 +- 1.0)
    }
  }

  test("a turtle that draws four sides, animated, keeps all of them") {
    run { t =>
      t.setPosition(-200, -100)
      (1 to 4).foreach { _ => t.forward(40); t.right() }
    }.map { t =>
      val b = t.turtlePath.getBounds()
      b.x should be(ox - 200 - 1.0 +- 1.0)
      b.width should be(42.0 +- 1.0)
      b.height should be(42.0 +- 1.0)
    }
  }

  test("after the pen was moved up and down, a new line starts where the turtle is") {
    run { t =>
      t.setPosition(100, 100)
      t.forward(30)
      t.penUp()
      t.forward(100)
      t.penDown()
      t.forward(30)
    }.map { t =>
      val b = t.turtlePath.getBounds()
      // 30 + 100 + 30 on the same line: the part in the middle is not drawn but the ends are
      b.x should be(ox + 100 - 1.0 +- 1.0)
      b.height should be(161.0 +- 1.5) // 160, and the pen width that PIXI 4 and 5 count differently
    }
  }
}
