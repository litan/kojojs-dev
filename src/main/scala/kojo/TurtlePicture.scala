package kojo

import scala.collection.mutable.ArrayBuffer

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.PrecisionModel

import kojo.doodle.Color

object TurtlePicture {
  def apply(fn: Turtle => Unit)(implicit turtleWorld: TurtleWorld): TurtlePicture = {
    val tp = new TurtlePicture
    tp.make(fn)
    tp
  }
}

class TurtlePicture(implicit val turtleWorld: TurtleWorld) extends Picture {
  val turtle = new Turtle(0, 0, true)
  val picLayer = turtle.turtleLayer
  val tnode = picLayer
  var made = false

  def make(fn: Turtle => Unit): Unit = {
    turtle.invisible()
    turtle.setAnimationDelay(0)
    fn(turtle)
    turtle.sync { () =>
      made = true
    }
  }
  def realDraw(): Unit = {
    turtleWorld.addTurtleLayer(picLayer)
    turtleWorld.render()
  }

  lazy val pmodel = new PrecisionModel(14)
  def newCoordinate(x: Double, y: Double) = {
    val coord = new Coordinate(x, y)
    pmodel.makePrecise(coord)
    coord
  }

  def initGeom(): Geometry = {
    val cab = new ArrayBuffer[Coordinate]
    val points = turtle.turtlePathPoints
    if (points.size > 1) {
      points.foreach { pt =>
        cab += newCoordinate(pt._1, pt._2)
      }
    }
    if (cab.size == 1) {
      cab += cab(0)
    }
    import scala.scalajs.js.JSConverters._
    Gf.createLineString(cab.toJSArray)
  }

  def setFillColor(c: Color): Unit = {
    turtle.turtlePath.tint = c.toRGBDouble
    turtleWorld.render()
  }
}
