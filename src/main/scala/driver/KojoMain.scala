package driver

object KojoMain {

  def main(args: Array[String]): Unit = {
    hunted()
  }

  def hunted(): Unit = {
    import kojo.{SwedishTurtle, Turtle, TurtleWorld, ColorMaker, Vector2D}
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val turtleWorld = new TurtleWorld()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    clear()
    drawStage(ColorMaker.khaki)
    val cb = canvasBounds

    def gameShape(color: Color) = PictureT { t =>
      import t._
      setFillColor(color)
      setPenColor(color)
      repeat(4) {
        forward(40)
        right(90)
      }
    }

    val r1 = gameShape(red)
    val r2 = gameShape(red)
    val r3 = gameShape(red)
    val r4 = gameShape(red)
    r1.setPosition(150, 150)
    r2.setPosition(-150, 150)
    r3.setPosition(0, 150)
    r4.setPosition(250, 0)

    val player = gameShape(blue)

    draw(r1, r2, r3, r4, player)

    val playerspeed = 9
    var vel1 = Vector2D(3, 2) * 2
    var vel2 = Vector2D(-3, 2) * 2
    var vel3 = Vector2D(0, 4) * 2
    var vel4 = Vector2D(4, 0) * 2

    val rs = Seq(r1, r2, r3, r4)
    var rsVels = Map(
      r1 -> vel1,
      r2 -> vel2,
      r3 -> vel3,
      r4 -> vel4
    )

    animate {
      rs.foreach { r =>
        r.translate(rsVels(r))
      }

      rs.foreach { r =>
        if (r.collidesWith(stageBorder)) {
          val newVel = bounceVecOffStage(rsVels(r), r)
          rsVels += (r -> newVel)
        }
      }

      rs.foreach { r =>
        if (player.collidesWith(r)) {
          gameLost()
        }
      }

      // player keyboard control
      if (isKeyPressed(Kc.VK_UP)) {
        player.translate(0, playerspeed)
      }

      if (isKeyPressed(Kc.VK_DOWN)) {
        player.translate(0, -playerspeed)
      }

      if (isKeyPressed(Kc.VK_LEFT)) {
        player.translate(-playerspeed, 0)
      }

      if (isKeyPressed(Kc.VK_RIGHT)) {
        player.translate(playerspeed, 0)
      }

      // player-border collision
      if (player.collidesWith(stageBorder)) {
        gameLost()
      }
    }

    def gameLost() {
      drawCenteredMessage("You Loose", purple, 20)
      stopAnimation()
      player.setFillColor(purple)
      player.scale(1.1)
    }

    showGameTime(60, "You loose", black)
    activateCanvas()
  }

  def pic1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, TurtleWorld, ColorMaker, Vector2D}
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val turtleWorld = new TurtleWorld()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    drawStage(green)
    val pic = PictureT { t =>
      t.setFillColor(red)
      t.setPenColor(ColorMaker.black)
      repeat(6) {
        t.forward(50)
        t.right(60)
      }
    }
    pic.draw()
    import turtleWorld.isKeyPressed
    val player = pic
    val playerspeed = 5
    animate {
      if (isKeyPressed(Kc.VK_UP)) {
        player.translate(0, playerspeed)
      }

      if (isKeyPressed(Kc.VK_DOWN)) {
        player.translate(0, -playerspeed)
      }

      if (isKeyPressed(Kc.VK_LEFT)) {
        player.translate(-playerspeed, 0)
      }

      if (isKeyPressed(Kc.VK_RIGHT)) {
        player.translate(playerspeed, 0)
      }
    }

    //    val pic2 = TurtlePicture { t =>
    //      t.setFillColor(green)
    //      t.setPenColor(ColorMaker.black)
    //      t.left(30)
    //      repeat(3) {
    //        t.forward(50)
    //        t.right(120)
    //      }
    //    }
    //    pic2.draw()
    //    pic2.translate(100, 130)

    //    val gp = GPics(pic, pic2)
    //    gp.draw()
    //
    //    var vel = Vector2D(10, 3)
    //    animate {
    //      if (pic.collidesWith(turtleWorld.stageBorder)) {
    //        vel = turtleWorld.bounceVecOffStage(vel, pic)
    //      }
    //      pic.translate(vel.x, vel.y)
    //    }

    //    animate {
    //      if (!pic.collidesWith(pic2)) {
    //        pic.translate(1, 3)
    //        //        pic2.translate(0, 0)
    //      }
    //    }

    //    dom.window.setTimeout({ () =>
    //      val str = LineString.asString(pic.picGeom.asInstanceOf[LineString])
    //      println(str)
    //    }, 2000)
  }

  def treeProgram(): Unit = {
    import kojo.{SwedishTurtle, Turtle, TurtleWorld, ColorMaker}
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val turtleWorld = new TurtleWorld()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

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

  def invisibleTest(): Unit = {
    import kojo.{SwedishTurtle, Turtle, TurtleWorld, ColorMaker}
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val turtleWorld = new TurtleWorld()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    forward(50)
    invisible()
    forward(50)
    visible()
    forward(50)
  }

  def vectorTest(): Unit = {
    val v1 = new com.vividsolutions.jts.geom.Vector2D(-3, 2)
    val v2 = v1.multiply(2)
    println(v2)
    val x = v2.getX()
    println(x)
    val nx = v2.normalize
    println(nx)
  }
}
