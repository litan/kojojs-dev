package kojo

import com.vividsolutions.jts.geom.AffineTransformation
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.LineString

import pixiscalajs.PIXI
import pixiscalajs.PIXI.Matrix

trait Picture {
  def tnode: PIXI.DisplayObject
  def made: Boolean
  def updateGeomTransform(): Unit = {
    pgTransform = t2t(tnode.localTransform)
  }
  def realDraw(): Unit
  def draw(): Unit = {
    realDraw()
    updateGeomTransform()
  }
  var pgTransform = new AffineTransformation
  def turtleWorld: TurtleWorld

  private def t2t(t: Matrix): AffineTransformation = {
    import scala.scalajs.js.JSConverters._
    val ms2 = Array.fill(6)(0.0).toJSArray
    ms2(0) = t.a // m00
    ms2(1) = t.c // m01
    ms2(2) = t.tx // m02
    ms2(3) = t.b // m10
    ms2(4) = t.d // m11
    ms2(5) = t.ty // m12
    new AffineTransformation(ms2)
  }

  def translate(dx: Double, dy: Double): Unit = {
    val pos = tnode.position
    pos.set(pos.x + dx, pos.y + dy)
    turtleWorld.render()
    updateGeomTransform()
  }

  def setPosition(x: Double, y: Double): Unit = {
    tnode.position.set(x, y)
    turtleWorld.render()
    updateGeomTransform()
  }

  def rotate(angle: Double): Unit = {
    val angleRads = Utils.deg2radians(angle)
    tnode.rotation += angleRads
    turtleWorld.render()
    updateGeomTransform()
  }

  var _picGeom: Geometry = _
  def initGeom(): Geometry
  def picGeom: Geometry = {
    if (!made) {
      return null
    }

    if (_picGeom == null) {
      try {
        _picGeom = initGeom()
      }
      catch {
        case ise: IllegalStateException =>
          throw ise
        case t: Throwable =>
          throw new IllegalStateException("Unable to create geometry for picture - " + t.getMessage, t)
      }
    }
    // TODO: next step is to support pgTransform
    pgTransform.transform(_picGeom)
  }

  def collidesWith(other: Picture): Boolean = {
    if (picGeom == null || other.picGeom == null) {
      false
    }
    else {
      val str1 = LineString.asString(picGeom.asInstanceOf[LineString])
      val str2 = LineString.asString(other.picGeom.asInstanceOf[LineString])
      val str3 = LineString.asString(other._picGeom.asInstanceOf[LineString])
      val ret = picGeom.intersects(other.picGeom)
      ret
    }
  }

  def collision(others: Seq[Picture]): Option[Picture] = {
    others.find { this collidesWith _ }
  }
}

object GPics {
  def apply(pics: List[Picture])(implicit turtleWorld: TurtleWorld) = new GPics(pics)
  def apply(pics: Picture*)(implicit turtleWorld: TurtleWorld) = new GPics(pics)
}

class GPics(pics: Seq[Picture])(implicit val turtleWorld: TurtleWorld) extends Picture {
  val tnode = new PIXI.Container()

  pics.foreach { p =>
    tnode.addChild(p.tnode)
  }

  def made: Boolean = {
    val notMade = pics.find { p => !p.made }
    notMade match {
      case Some(_) => false
      case None    => true
    }
  }

  def realDraw(): Unit = {
    turtleWorld.addTurtleLayer(tnode)
    turtleWorld.render()
    pics.foreach { p =>
      p.updateGeomTransform()
    }
  }

  def initGeom() = {
    var pg = pics(0).picGeom
    pics.tail.foreach { pic =>
      pg = pg union pic.picGeom
    }
    pg
  }
}
