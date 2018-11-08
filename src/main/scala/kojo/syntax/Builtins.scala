package kojo.syntax

import org.scalajs.dom.window

import kojo.KeyCodes
import kojo.Picture
import kojo.SwedishTurtle
import kojo.Turtle
import kojo.TurtlePicture
import kojo.TurtleWorld
import kojo.Vector2D
import kojo.doodle.Color
import pixiscalajs.PIXI.Rectangle

class Builtins(implicit turtleWorld: TurtleWorld) {
  val turtle = new Turtle(0, 0)
  val svTurtle = new SwedishTurtle(turtle)
  val Color = kojo.doodle.Color
  val noColor = Color(0, 0, 0, 0)

  val Random = new java.util.Random

  def random(upperBound: Int) = Random.nextInt(upperBound)

  def randomDouble(upperBound: Int) = Random.nextDouble * upperBound

  def randomBoolean = Random.nextBoolean

  def randomFrom[T](seq: Seq[T]) = seq(random(seq.length))

  def randomColor = Color(random(256), random(256), random(256))

  def randomTransparentColor = Color(random(256), random(256), random(256), 100 + random(156))

  def readln(prompt: String): String = {
    val ret = window.prompt(prompt, "Type here")
    if (ret == null)
      throw new RuntimeException("Read failed.")
    else
      ret
  }

  def readInt(prompt: String): Int = readln(prompt).toInt

  def readDouble(prompt: String): Double = readln(prompt).toDouble

  val setBackground = turtleWorld.setBackground _
  val animate = turtleWorld.animate _
  val drawStage = turtleWorld.drawStage _

  val bounceVecOffStage = turtleWorld.bounceVecOffStage _
  def bouncePicVectorOffPic(pic: Picture, v: Vector2D, obstacle: Picture): Vector2D =
    turtleWorld.bouncePicVectorOffPic(pic, v, obstacle, Random)
  def bouncePicVectorOffStage(p: Picture, v: Vector2D): Vector2D = bouncePicVectorOffPic(p, v, turtleWorld.stageArea)

  val isKeyPressed = turtleWorld.isKeyPressed _
  lazy val stageBorder = turtleWorld.stageBorder
  val Kc = new KeyCodes
  val canvasBounds = {
    val pos = turtleWorld.stage.position
    new Rectangle(pos.x, pos.y, turtleWorld.width, turtleWorld.height)
  }
  def PictureT(fn: Turtle => Unit)(implicit turtleWorld: TurtleWorld): TurtlePicture = {
    TurtlePicture(fn)
  }
  def drawCenteredMessage(message: String, color: Color = Color.black, fontSize: Int = 15) {
    // Todo
  }
  def showGameTime(limitSecs: Int, endMsg: String, color: Color = Color.black, fontSize: Int = 15): Unit = {
    // Todo
  }
  def activateCanvas(): Unit = {
    // Todo
  }
  val stopAnimation = turtleWorld.stopAnimation _
  def draw(pictures: Picture*) = pictures.foreach { _ draw () }
  def draw(pictures: IndexedSeq[Picture]) = pictures.foreach { _ draw () }
  def draw(pictures: List[Picture]) = pictures.foreach { _ draw () }
  def cleari(): Unit = {}
}
