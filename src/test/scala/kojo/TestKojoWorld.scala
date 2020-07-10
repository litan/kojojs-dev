package kojo
import java.util.Random

import com.vividsolutions.jts.geom.Coordinate
import kojo.doodle.Color
import org.scalajs.dom.window
import pixiscalajs.PIXI
import pixiscalajs.PIXI.interaction.InteractionData
import pixiscalajs.PIXI.{Container, DisplayObject, Point, Rectangle}

import scala.scalajs.js

class TestKojoWorld extends KojoWorld {
  def canvasWidth = 800
  def canvasHeight = 600

  private val stage = new PIXI.Container()
  stage.name = "Stage"
  stage.width = canvasWidth
  stage.height = canvasHeight
  stage.interactive = false
  stage.setTransform(canvasWidth / 2, canvasHeight / 2, 1, -1, 0, 0, 0, 0, 0)

  def addLayer(layer: Container): Unit = {
    stage.addChild(layer)
  }
  def removeLayer(layer: Container): Unit = {
    stage.removeChild(layer)
  }
  def scheduleLater(fn: => Unit): Unit = {
    window.setTimeout(() => fn, 0)
  }
  def runLater(ms: Double)(fn: => Unit): Unit = {
    window.setTimeout(() => fn, ms)
  }
  def render(): Unit = {
  }

  def moveToFront(obj: DisplayObject): Unit = {

  }

  def moveToBack(obj: DisplayObject): Unit = {

  }

  def setBackground(color: Color): Unit = {

  }

  def animate(fn: => Unit): Unit = {

  }

  def timer(ms: Long)(fn: => Unit): Unit = {

  }

  def stopAnimation(): Unit = {

  }

  val noPic = TurtlePicture { t =>
  }(this)

  @volatile var stageBorder: Picture = _
  @volatile var stageLeft: Picture = _
  @volatile var stageTop: Picture = _
  @volatile var stageRight: Picture = _
  @volatile var stageBot: Picture = _
  @volatile var stageArea: Picture = _

  def clearStage() {
    stageBorder = noPic
    stageLeft = noPic
    stageTop = noPic
    stageRight = noPic
    stageBot = noPic
  }

  def drawStage(fillc: Color)(implicit kojoWorld: KojoWorld) {
    def left(size: Double) = TurtlePicture { t =>
      t.setPenThickness(0)
      t.forward(size)
    }
    def top(size: Double) = TurtlePicture { t =>
      t.setPenThickness(0)
      t.right()
      t.forward(size)
    }
    def right(size: Double) = TurtlePicture { t =>
      t.setPenThickness(0)
      t.right(180)
      t.forward(size)
    }
    def bottom(size: Double) = TurtlePicture { t =>
      t.setPenThickness(0)
      t.left()
      t.forward(size)
    }

    val xmax = stage.position.x.abs
    val ymax = stage.position.y.abs

    stageLeft = left(canvasHeight)
    stageLeft.translate(-xmax, -ymax)

    stageTop = top(canvasWidth)
    stageTop.translate(-xmax, ymax)

    stageRight = right(canvasHeight)
    stageRight.translate(xmax, ymax)

    stageBot = bottom(canvasWidth)
    stageBot.translate(xmax, -ymax)

    stageArea = TurtlePicture { t =>
      t.setFillColor(fillc)
      t.setPenColor(Color.darkGray)
      for (_ <- 1 to 2) {
        t.forward(canvasHeight)
        t.right()
        t.forward(canvasWidth)
        t.right()
      }
    }
    stageArea.translate(-xmax, -ymax)

    stageBorder = GPics(
      stageLeft,
      stageTop,
      stageRight,
      stageBot
    )

    stageArea.draw()
    stageBorder.draw()
  }

  def bounceVecOffStage(v: Vector2D, p: Picture): Vector2D = {
    val topCollides = p.collidesWith(stageTop)
    val leftCollides = p.collidesWith(stageLeft)
    val botCollides = p.collidesWith(stageBot)
    val rightCollides = p.collidesWith(stageRight)

    val c = v.magnitude / math.sqrt(2)
    if (topCollides && leftCollides)
      Vector2D(c, -c)
    else if (topCollides && rightCollides)
      Vector2D(-c, -c)
    else if (botCollides && leftCollides)
      Vector2D(c, c)
    else if (botCollides && rightCollides)
      Vector2D(-c, c)
    else if (topCollides)
      Vector2D(v.x, -v.y)
    else if (botCollides)
      Vector2D(v.x, -v.y)
    else if (leftCollides)
      Vector2D(-v.x, v.y)
    else if (rightCollides)
      Vector2D(-v.x, v.y)
    else
      v
  }

  def collidesWithStage(p: Picture): Boolean = {
    val stageparts = List(stageTop, stageBot, stageLeft, stageRight)
    p.collision(stageparts).isDefined
  }

  def bouncePicVectorOffPic(pic: Picture, vel: Vector2D, obstacle: Picture, rg: Random): Vector2D = {
    // returns points on the obstacle that contain the given collision coordinate
    def obstacleCollPoints(c: Coordinate): Option[js.Array[Coordinate]] = {
      obstacle.picGeom.getCoordinates.sliding(2).find { cs =>
        val xcheck = if (cs(0).x > cs(1).x)
          cs(0).x >= c.x && c.x >= cs(1).x
        else
          cs(0).x <= c.x && c.x <= cs(1).x

        val ycheck = if (cs(0).y > cs(1).y)
          cs(0).y >= c.y && c.y >= cs(1).y
        else
          cs(0).y <= c.y && c.y <= cs(1).y
        xcheck && ycheck
      }
    }
    // returns vector for obstacle boundary segment that contains the collision point
    def obstacleCollVector(c: Coordinate) = makeVectorFromCollPoints(obstacleCollPoints(c))

    // creates a vector out of two (collision) points
    def makeVectorFromCollPoints(cps: Option[js.Array[Coordinate]]) = cps match {
      case Some(cs) =>
        Vector2D(cs(0).x - cs(1).x, cs(0).y - cs(1).y)
      case None =>
        println("Warning: unable to determine collision vector; generating random vector")
        Vector2D(rg.nextDouble, rg.nextDouble)
    }

    def collisionVector = {
      val pt = obstacle.intersection(pic)
      val iCoords = pt.getCoordinates

      if (iCoords.length == 0) {
        Vector2D(rg.nextDouble, rg.nextDouble).normalize
      }
      else {
        if (iCoords.length == 1) {
          val cv1 = obstacleCollVector(iCoords(0))
          cv1.normalize
        }
        else {
          val c1 = iCoords(0)
          val c2 = iCoords(iCoords.length - 1)
          makeVectorFromCollPoints(Some(js.Array(c1, c2))).normalize
        }
      }
    }
    def pullbackCollision() = {
      val velNorm = vel.normalize
      val v2 = velNorm.rotate(180)
      val velMag = vel.magnitude
      var pulled = 0
      while (pic.collidesWith(obstacle) && pulled < velMag) {
        pic.offset(v2)
        pulled += 1
      }
      pic.offset(velNorm)
    }

    pullbackCollision()
    val cv = collisionVector
    vel.bounceOff(cv)
  }

  def isKeyPressed(keyCode: Int) = false

  def stagePosition = stage.position
  def positionOnStage(data: InteractionData) = data.getLocalPosition(stage)

  def isAMouseButtonPressed = false

  def mouseMoveOnlyWhenInside(on: Boolean): Unit = {}
  def erasePictures(): Unit = {}
  def mouseXY: Point = Point(0, 0)
  def setup(fn: => Unit): Unit = {}
  def size(width: Double, height: Double): Unit = {}
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double): Unit = {}
  def toggleFullScreenCanvas(): Unit = {}
  def canvasBounds: Rectangle = new Rectangle(0, 0, 1, 1)
}
