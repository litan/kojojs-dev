package kojo

import kojo.doodle.Color

trait TurtleAPI {
  def forward(n: Double): Unit
  def right(): Unit
  def left(): Unit
  def right(angle: Double): Unit
  def left(angle: Double): Unit
  def setPenColor(color: Color): Unit
  def setFillColor(color: Color): Unit
  def clear(): Unit
  def setPenThickness(t: Double): Unit
  def setAnimationDelay(delay: Long): Unit
  def invisible(): Unit
  def visible(): Unit
}
