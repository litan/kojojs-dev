package kojo.shape 

// intended to work as the desktop Point
// https://github.com/litan/kojo/blob/master/src/main/scala/net/kogics/kojo/core/shapes.scala#L35
case class Point(x: Double, y: Double){
  def +(that: Point): Point = Point(this.x + that.x, this.y + that.y)
  def -(that: Point): Point = Point(this.x - that.x, this.y - that.y)
  def unary_- : Point = Point(-x, -y)
  def onX: Point   = Point(x, 0)
  def onY: Point   = Point(0, y)
} 