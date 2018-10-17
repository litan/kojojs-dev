package driver

object KojoMain {

  def main(args: Array[String]): Unit = {
    treeProgram()
  }

  def treeProgram(): Unit = {
    import kojo.{SwedishTurtle, Turtle, TurtleWorld, ColorMaker}
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins._
    implicit val turtleWorld = new TurtleWorld()
    val turtle = new Turtle(0, 0)
    val svTurtle = new SwedishTurtle(turtle)
    import turtle._
    import svTurtle._
    def setBackground(color: Color): Unit = turtleWorld.setBackground(color)

    def tree(distance: Double) {
      if (distance > 4) {
        setPenThickness(distance / 7)
        setPenColor(Color.rgb(distance.toInt, math.abs(255 - distance * 3).toInt, 125))
        forward(distance)
        right(25)
        tree(distance * 0.8 - 2)
        left(45)
        tree(distance - 10)
        right(20)
        forward(-distance)
      }
    }

    clear()
    setAnimationDelay(0)
    setBackground(ColorMaker.azure)
    // hop(200)
    tree(90)

  }

}
