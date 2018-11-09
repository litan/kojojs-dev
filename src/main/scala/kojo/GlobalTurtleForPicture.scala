package kojo

import kojo.doodle.Color

class GlobalTurtleForPicture extends TurtleAPI {
  var globalTurtle: Turtle = _
  def forward(n: Double): Unit = globalTurtle.forward(n)
  def right(): Unit = globalTurtle.right()
  def left(): Unit = globalTurtle.left()
  def right(angle: Double): Unit = globalTurtle.right(angle)
  def left(angle: Double): Unit = globalTurtle.left(angle)
  def setPenColor(color: Color): Unit = globalTurtle.setPenColor(color)
  def setFillColor(color: Color): Unit = globalTurtle.setFillColor(color)
  def clear(): Unit = globalTurtle.clear()
  def setPenThickness(t: Double): Unit = globalTurtle.setPenThickness(t)
  def setAnimationDelay(delay: Long): Unit = globalTurtle.setAnimationDelay(delay)
  def invisible(): Unit = globalTurtle.invisible()
  def visible(): Unit = globalTurtle.visible()
}
