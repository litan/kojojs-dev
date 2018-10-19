package kojo

class TurtlePicture(implicit turtleWorld: TurtleWorld) {
  val turtle = new Turtle(0, 0, true)
  val picLayer = turtle.turtleLayer

  def make(fn: Turtle => Unit): Unit = {
    turtle.setAnimationDelay(0)
    fn(turtle)
  }
  def draw(): Unit = {
    turtleWorld.addTurtleLayer(picLayer)
    turtleWorld.render()
  }

  def translate(x: Double, y: Double): Unit = {
    val pos = picLayer.position
    picLayer.position.set(pos.x + x, pos.y + y)
    turtleWorld.render()
  }

  def rotate(angle: Double): Unit = {
    val angleRads = Utils.deg2radians(angle)
    picLayer.rotation += angleRads
    turtleWorld.render()
  }
}
