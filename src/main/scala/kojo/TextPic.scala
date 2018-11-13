package kojo

import com.vividsolutions.jts.geom.Geometry

import kojo.doodle.Color
import pixiscalajs.PIXI

class TextPic(text: Any, fontSize: Int, color: Color)(implicit val turtleWorld: TurtleWorld) extends Picture {
  val tnode = {
    val pixiText = new PIXI.Text(text.toString)
    pixiText.setTransform(0, 0, 1, -1, 0, 0, 0, 0, 0)
    pixiText.style.fontSize = fontSize
    pixiText.style.fill = color.toCanvas
    made = true
    pixiText
  }

  var made = false

  override def realDraw(): Unit = {
    val textLayer = new PIXI.Container
    textLayer.addChild(tnode)
    turtleWorld.addTurtleLayer(textLayer)
    turtleWorld.render()
  }

  override def setFillColor(c: Color): Unit = {
    tnode.style.fill = color.toCanvas
  }

  override def initGeom(): Geometry = {
    null
  }

  def update(text: Any): Unit = {
    tnode.text = text.toString
    turtleWorld.render()
  }
}
