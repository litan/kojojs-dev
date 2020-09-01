package driver

import kojo.Utils

import scala.scalajs.js

object KojoMain {

  def main(args: Array[String]): Unit = {
    huntedWithScreens()
  }

  def hunted(): Unit = {
    import kojo.RepeatCommands._
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()

    drawStage(ColorMaker.khaki)
    val cb = canvasBounds

    //    def gameShape(color: Color) = PictureT { t =>
    //      import t._
    //      setFillColor(color)
    //      setPenColor(color)
    //      repeat(4) {
    //        forward(40)
    //        right(90)
    //      }
    //    }
    def gameShape(color: Color) = fillColor(color) * penColor(color) -> Picture.circle(20)

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
        r.offset(rsVels(r))
      }

      rs.foreach { r =>
        if (r.collidesWith(stageBorder)) {
          val newVel = bouncePicOffStage(r, rsVels(r))
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
        // gameLost()
      }
    }

    def gameLost() {
      drawCenteredMessage("You Loose", purple, 20)
      stopAnimation()
      player.setFillColor(purple)
      player.scale(1.1)
    }

    showGameTime(60, "You Win", black)
    showFps(black)
    activateCanvas()
  }

  def pic1(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._

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
    import kojoWorld.isKeyPressed
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
    //      if (pic.collidesWith(kojoWorld.stageBorder)) {
    //        vel = kojoWorld.bounceVecOffStage(vel, pic)
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
    import kojo.KojoWorldImpl
    import kojo.doodle.Color
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

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
    import kojo.KojoWorldImpl
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

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

  def bounce1(): Unit = {
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._

    val p1 = PictureT { t =>
      import t._
      repeat(4) {
        forward(100)
        right(90)
      }
    }

    val p2 = PictureT { t =>
      import t._
      repeat(4) {
        forward(100)
        right(90)
      }
    }

    draw(p1, p2)
    p2.setPosition(150, 100)

    var vel1 = Vector2D(1, 1)

    animate {
      if (p1.collidesWith(p2)) {
        vel1 = bouncePicVectorOffPic(p1, vel1, p2)
      }
      p1.translate(vel1)
      p2.translate(-vel1)
    }

  }

  def bounce2(): Unit = {
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    drawStage(ColorMaker.darkKhaki)
    val obj = fillColor(red) * penColor(red) -> Picture.rectangle(20, 30)
    //    obj.setPenColor(red)
    //    obj.setFillColor(red)
    draw(obj)

    var velocity = Vector2D(3, 3)

    animate {
      obj.translate(velocity)
      if (obj.collidesWith(stageBorder)) {
        velocity = bouncePicVectorOffStage(obj, velocity).normalize
      }
      else {
        velocity = (velocity * 1.1).limit(20)
      }

      if (isKeyPressed(Kc.VK_B)) {
        velocity = velocity.normalize
      }
    }
    activateCanvas()
  }

  def square(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.Speed._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    clear()
    setSpeed(fast)
    repeat(4) {
      forward(100)
      right(90)
    }
  }

  def picTextU(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    clear()
    invisible()
    val msg = "Hello"
    val text = penColor(cm.green) -> Picture.textu(msg, 25, Color.blue)
    draw(text)
    var x = 1
    animate {
      //      text.update(msg + x)
      x += 1
    }
  }

  def picTextU2(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    clear()
    invisible()
    val msg = "Hello"
    val text = Picture.textu(msg, 25, Color.blue)
    draw(text)
    var x = 1
    timer(1000) {
      text.update(msg + x)
      x += 1
    }
  }

  def textPicPenFill(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val msg = "Hello"
    val text = Picture.textu(msg, 25, Color.blue)
    draw(text)
    text.setFillColor(green)
    text.setPenColor(red)
    text.setPenThickness(4)
  }

  def turtlePicPenFill(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic = Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    draw(pic)
    pic.setFillColor(yellow)
    pic.setPenColor(blue)
    pic.setPenThickness(14)
  }

  def transformers1(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic0 = penColor(red) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic1 = penColor(blue) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic2 = penColor(green) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    pic1.rotate(30)
    pic1.translate(100, 0)
    pic2.translate(100, 0)
    pic2.rotate(30)
    draw(pic0, pic1, pic2)
  }

  def transformers2(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic0 = penColor(red) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic1 = penColor(blue) * rot(30) * trans(100, 0) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic2 = penColor(green) * trans(100, 0) * rot(30) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    draw(pic0, pic1, pic2)
  }

  def transformers3(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic0 = penColor(red) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic1 = penColor(blue) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic2 = penColor(green) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    draw(pic0, pic1, pic2)
    pic1.scale(2)
    pic1.translate(100, 0)
    pic2.translate(100, 0)
    pic2.scale(2)
  }

  def transformers4(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic0 = penColor(red) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic1 = penColor(blue) * scale(2) * trans(100, 0) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic2 = penColor(green) * trans(100, 0) * scale(2) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    draw(pic0, pic1, pic2)
  }

  def translate(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = penColor(blue) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    val pic2 = penColor(green) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    draw(pic1)
    draw(pic2)
    pic1.rotate(30)
    pic1.translate(100, 0)
    //    pic1.rotate(30)
    //    pic2.rotate(30)
    //    pic2.translate(10, 50)
  }

  def pong(): Unit = {
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    // A Game of Pong
    // Player on right uses Up/Down Arrow keys to control paddle
    // Player on left uses A/Z keys to control paddle
    // Press 'Esc' to quit
    cleari()
    drawStage(Color(0, 0, 250, 20))

    val glevel = 1
    val PaddleH = 100
    val PaddleW = 25
    val BallR = 15
    val Height = canvasBounds.height
    val Width = canvasBounds.width
    val PaddleSpeed = 5
    val BallSpeed = 5

    def paddle = penColor(darkGray) * fillColor(red) -> Picture.rect(PaddleH, PaddleW)
    def vline = penColor(darkGray) -> Picture.vline(Height)
    def ball = penColor(lightGray) * penWidth(1) * fillColor(Color(0, 230, 0)) -> Picture.circle(BallR)
    def levelFactor = math.pow(1.1, glevel)

    case class PaddleS(speed: Double, lastUp: Boolean) { outer =>
      def incrSpeed(i: Double) = copy(speed = outer.speed + i)
      def scaleSpeed(f: Double) = copy(speed = outer.speed * f)
    }

    case class Score(score: Int, left: Boolean) { outer =>
      val xt = if (left) -50 else 50
      val pScore = trans(xt, Height / 2 - 10) * penColor(black) -> Picture.textu(score, 20)
      def incrScore = copy(score = outer.score + 1)
    }

    case class Level(num: Int, vel: Vector2D) {
      val pLevel = trans(-60, -Height / 2 + 40) * penColor(black) -> Picture.textu(s"Level: $num", 20)
    }

    case class World(
      ballVel:    Vector2D,
      level:      Level,
      paddleInfo: Map[Picture, PaddleS],
      scores:     Map[Picture, Score]
    )

    val topbot = Seq(stageTop, stageBot)
    val paddle1 = trans(-Width / 2, 0) -> paddle
    val paddle2 = trans(Width / 2 - PaddleW, 0) -> paddle
    val centerLine = trans(0, -Height / 2) -> vline
    val leftGutter = trans(-Width / 2 + PaddleW, -Height / 2) -> vline
    val rightGutter = trans(Width / 2 - PaddleW, -Height / 2) -> vline
    val gutters = Seq(leftGutter, rightGutter)
    val paddles = Seq(paddle1, paddle2)
    val gameBall = ball

    draw(paddle1, paddle2, centerLine, leftGutter, rightGutter, gameBall)

    val ballVel = Vector2D(BallSpeed * levelFactor, 3)
    var world = World(
      ballVel,
      Level(glevel, ballVel),
      Map(
        paddle1 -> PaddleS(PaddleSpeed * levelFactor, true),
        paddle2 -> PaddleS(PaddleSpeed * levelFactor, true)
      ),
      Map(
        paddle1 -> Score(0, true),
        paddle2 -> Score(0, false)
      )
    )

    draw(world.scores(paddle1).pScore)
    draw(world.scores(paddle2).pScore)
    draw(world.level.pLevel)

    animate {
      gameBall.translate(world.ballVel)
      if (gameBall.collision(paddles).isDefined) {
        world = world.copy(
          ballVel = Vector2D(-world.ballVel.x, world.ballVel.y)
        )
      }
      else if (gameBall.collision(topbot).isDefined) {
        world = world.copy(
          ballVel = Vector2D(world.ballVel.x, -world.ballVel.y)
        )
      }
      else if (gameBall.collidesWith(leftGutter)) {
        gameBall.setPosition(0, 0)
        world.scores(paddle2).pScore.erase()
        world = world.copy(
          ballVel = Vector2D(-world.level.vel.x.abs, world.level.vel.y),
          scores = world.scores + (paddle2 -> world.scores(paddle2).incrScore)
        )
        draw(world.scores(paddle2).pScore)
      }
      else if (gameBall.collidesWith(rightGutter)) {
        gameBall.setPosition(0, 0)
        world.scores(paddle1).pScore.erase()
        world = world.copy(
          ballVel = Vector2D(world.level.vel.x.abs, world.level.vel.y),
          scores = world.scores + (paddle1 -> world.scores(paddle1).incrScore)
        )
        draw(world.scores(paddle1).pScore)
      }
      else {
        world = world.copy(
          ballVel = (world.ballVel * 1.001).limit(10)
        )
      }
      paddleBehavior(paddle1, Kc.VK_A, Kc.VK_Z)
      paddleBehavior(paddle2, Kc.VK_UP, Kc.VK_DOWN)
    }

    def paddleBehavior(paddle: Picture, upkey: Int, downkey: Int) {
      val pstate = world.paddleInfo(paddle)
      if (isKeyPressed(upkey) && !paddle.collidesWith(stageTop)) {
        paddle.translate(0, pstate.speed)
        if (pstate.lastUp) {
          world = world.copy(
            paddleInfo = world.paddleInfo + (paddle -> pstate.incrSpeed(0.1))
          )
        }
        else {
          world = world.copy(
            paddleInfo = world.paddleInfo + (paddle -> PaddleS(PaddleSpeed, true))
          )
        }
      }
      if (isKeyPressed(downkey) && !paddle.collidesWith(stageBot)) {
        paddle.translate(0, -pstate.speed)
        if (!pstate.lastUp) {
          world = world.copy(
            paddleInfo = world.paddleInfo + (paddle -> pstate.incrSpeed(0.1))
          )
        }
        else {
          world = world.copy(
            paddleInfo = world.paddleInfo + (paddle -> PaddleS(PaddleSpeed, false))
          )
        }
      }
    }
    activateCanvas()
  }

  def pong2(): Unit = {
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    drawStage(ColorMaker.black)

    val cb = canvasBounds
    val pw = 30
    val ph = 90
    val br = 20
    val ps = 5
    var bv = Vector2D(5, 2) * 1.5
    val paddle1 = trans(cb.x + cb.width - pw, -ph / 2) -> Picture.rectangle(pw, ph)
    val paddle2 = trans(cb.x, -ph / 2) -> Picture.rectangle(pw, ph)
    Seq(paddle1, paddle2).foreach { p =>
      p.setPenColor(ColorMaker.blue)
      p.setFillColor(ColorMaker.blue)
    }
    val ball = Picture.circle(br)
    ball.setPenColor(ColorMaker.red)
    ball.setFillColor(ColorMaker.red)
    draw(paddle1, paddle2, ball)

    def movePaddle(paddle: Picture, stageBoundary: Picture, dy: Int) {
      paddle.translate(0, dy)
      while (paddle.collidesWith(stageBoundary)) {
        paddle.translate(0, -dy)
      }
    }

    val obstacles = Vector(stageTop, stageBot, paddle1, paddle2)

    animate {
      if (isKeyPressed(Kc.VK_UP)) {
        movePaddle(paddle1, stageTop, ps)
      }
      else if (isKeyPressed(Kc.VK_DOWN)) {
        movePaddle(paddle1, stageBot, -ps)
      }
      if (isKeyPressed(Kc.VK_A)) {
        movePaddle(paddle2, stageTop, ps)
      }
      else if (isKeyPressed(Kc.VK_Z)) {
        movePaddle(paddle2, stageBot, -ps)
      }

      ball.translate(bv)

      if (ball.collidesWith(stageRight) || ball.collidesWith(stageLeft)) {
        ball.setFillColor(black)
        stopAnimation()
      }

      obstacles.foreach { obstacle =>
        while (ball.collidesWith(obstacle)) {
          bv = bouncePicVectorOffPic(ball, bv, obstacle)
          ball.translate(bv)
        }
      }
    }

    activateCanvas()
  }

  def fillRect() {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = fillColor(blue) -> Picture {
      repeat(5) {
        forward(100)
        right()
      }
    }

    draw(pic1)
    pic1.setFillColor(green)
  }

  def mondrian(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    // Based on ideas from https://generativeartistry.com/tutorials/piet-mondrian/

    cleari()
    val cb = canvasBounds

    val white = Color(0xF2F5F1)
    val colors = Seq(Color(0xD40920), Color(0x1356A2), Color(0xF7D842))

    case class Rectangle(x: Double, y: Double, width: Double,
                         height: Double, var c: Color = white)

    def rectPic(r: Rectangle) = {
      penColor(black) * fillColor(r.c) * penWidth(10) * trans(r.x, r.y) ->
        Picture.rectangle(r.width, r.height)
    }

    def splitRectsX(rects: Vector[Rectangle], x: Double) = {
      var ret = rects
      repeatFor(rects.length - 1 to 0 by -1) { i =>
        val square = rects(i)
        if (x > square.x && x < square.x + square.width) {
          if (randomBoolean) {
            ret = ret.slice(0, i) ++ ret.slice(i + 1, ret.length)
            ret = ret ++ splitOnX(square, x)
          }
        }
      }
      ret
    }

    def splitRectsY(rects: Vector[Rectangle], y: Double) = {
      var ret = rects
      repeatFor(rects.length - 1 to 0 by -1) { i =>
        val square = rects(i)
        if (y > square.y && y < square.y + square.height) {
          if (randomBoolean) {
            ret = ret.slice(0, i) ++ ret.slice(i + 1, ret.length)
            ret = ret ++ splitOnY(square, y)
          }
        }
      }
      ret
    }

    def splitOnX(square: Rectangle, splitAt: Double) = {
      val squareA = Rectangle(
        square.x,
        square.y,
        square.width - (square.width - splitAt + square.x),
        square.height
      )

      val squareB = Rectangle(
        splitAt,
        square.y,
        square.width - splitAt + square.x,
        square.height
      )

      Vector(squareA, squareB)
    }

    def splitOnY(square: Rectangle, splitAt: Double) = {
      val squareA = Rectangle(
        square.x,
        square.y,
        square.width,
        square.height - (square.height - splitAt + square.y)
      )

      val squareB = Rectangle(
        square.x,
        splitAt,
        square.width,
        square.height - splitAt + square.y
      )

      Vector(squareA, squareB)
    }

    var rects = Vector(Rectangle(cb.x, cb.y, cb.width, cb.height))
    val n = 20
    val deltax = cb.width / (n + 1)
    val deltay = cb.height / (n + 1)

    repeatFor(1 to n) { i =>
      rects = splitRectsX(rects, cb.x + i * deltax)
      rects = splitRectsY(rects, cb.y + i * deltay)
    }
    // println(s"rects made: ${rects.size}; drawing")
    colors.foreach { c =>
      val idx = Math.floor(randomDouble(1) * rects.length).toInt
      rects(idx).c = c
    }
    GPics()
    draw(rects.map(rectPic))
  }

  def ballAccel(): Unit = {
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    drawStage(ColorMaker.darkKhaki)
    val obj = Picture {
      setPenColor(red)
      setFillColor(red)
      repeat(4) {
        forward(30)
        right(90)
      }
    }
    draw(obj)

    var velocity = Vector2D(3, 1) * 0.1

    animate {
      obj.translate(velocity)
      if (obj.collidesWith(stageBorder)) {
        velocity = bouncePicVectorOffStage(obj, velocity).normalize
      }
      else {
        velocity = (velocity * 1.1).limit(5)
      }

      if (isKeyPressed(Kc.VK_B)) {
        velocity = velocity.normalize
        stopAnimation()
      }
    }

    activateCanvas()
  }

  def lineJoin(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    setPenThickness(10)
    repeat(4) {
      forward(100)
      right()
    }
  }

  def noPenColor(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    def p = Picture {
      left(30)
      repeat(3) {
        forward(100)
        right(120)
      }
    }

    val pic = penColor(noColor) * fillColor(green) -> p
    draw(pic)
  }

  def pointyBounce(): Unit = {
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    val player = fillColor(red) -> Picture {
      forward(100)
      right(90)
      forward(100)
    }

    cleari()
    drawStage(blue)
    draw(player)

    var vel = Vector2D(1, 2)

    animate {
      player.translate(vel)
      if (player.collidesWith(stageBorder)) {
        vel = bouncePicVectorOffStage(player, vel)
      }
    }
  }

  def pacman(): Unit = {
    import kojo.RepeatCommands._
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    // todo
    // pic moveToFront
    // pic copy
    // turtle saveStyle/restoreStyle
    // Picture.circle

    val tileSize = 32

    //    def centeredCirc(r: Double, fill: Color, boundary: Color) =
    //      penColor(boundary) * fillColor(fill) -> Picture.circle(r)

    def centeredCirc(r: Double, fill: Color, boundary: Color) {
      setFillColor(fill)
      setPenColor(boundary)
      right(90)
      hop(tileSize / 2 - r)
      left(90)
      hop(tileSize / 2 - r)
      repeat(4) {
        forward(2 * r)
        right(90)
      }
    }

    def wallPic = Picture {
      setPenColor(blue)
      setFillColor(Color(16, 34, 157))
      repeat(4) {
        forward(tileSize)
        right()
      }
    }

    def foodPic = Picture {
      centeredCirc(3, white, white)
    }

    def capsulePic = Picture {
      centeredCirc(7, white, yellow)
    }

    def ghostPic = Picture {
      centeredCirc(12, Color(255, 16, 42), white)
    }

    def pacmanPic = Picture {
      centeredCirc(12, Color(255, 244, 26), black)
    }

    def blankPic = Picture {
      forward(0)
    }

    val picMap = Map(
      '%' -> wallPic _,
      'o' -> capsulePic _,
      '.' -> foodPic _,
      'G' -> ghostPic _,
      'P' -> pacmanPic _,
      ' ' -> blankPic _
    )

    val layout = """
%%%%%%%%%%%%%%%%%%%%
%o...%........%....%
%.%%.%.%%%%%%.%.%%.%
%.%.......G......%.%
%.%.%%.%%  %%.%%.%.%
%...........%.....P%
%.%.%%.%%%%%%.%%.%.%
%.%...G............%
%.%%.%.%%%%%%.%.%%.%
%....%........%...o%
%%%%%%%%%%%%%%%%%%%%
"""

    case class Position(x: Int, y: Int)
    trait Entity {
      def position: Position
      def pic: Picture
    }
    abstract class DynamicEntity(var position: Position, pic: Picture) extends Entity {
      val Left = 1; val Right = 2; val Up = 3; val Down = 4
      var prevMove = -1
      def moveUp() {
        position = position.copy(y = position.y + 1)
        pic.translate(0, tileSize)
        prevMove = Up
      }
      def moveDown() {
        position = position.copy(y = position.y - 1)
        pic.translate(0, -tileSize)
        prevMove = Down
      }
      def moveLeft() {
        position = position.copy(x = position.x - 1)
        pic.translate(-tileSize, 0)
        prevMove = Left
      }
      def moveRight() {
        position = position.copy(x = position.x + 1)
        pic.translate(tileSize, 0)
        prevMove = Right
      }
    }
    abstract class StaticEntity(val position: Position, pic: Picture) extends Entity

    case class PacMan(x: Int, y: Int, pic: Picture)
      extends DynamicEntity(Position(x, y), pic) {
      def maybeEatFood() {
        val newPos = position
        if (gameState.isFood(newPos.x, newPos.y)) {
          //          gameState.eraseFood(newPos.x, newPos.y)
        }
      }
      def moveContinue() {
        //        val pos = position
        //        if (prevMove == Left && !gameState.isWall(pos.x - 1, pos.y)) {
        //          moveLeft()
        //        }
        //        else if (prevMove == Right && !gameState.isWall(pos.x + 1, pos.y)) {
        //          moveRight()
        //        }
        //        else if (prevMove == Up && !gameState.isWall(pos.x, pos.y + 1)) {
        //          moveUp()
        //        }
        //        else if (prevMove == Down && !gameState.isWall(pos.x, pos.y - 1)) {
        //          moveDown()
        //        }
      }
    }
    case class Ghost(x: Int, y: Int, pic: Picture)
      extends DynamicEntity(Position(x, y), pic) {
      def moveRandom() {
        val pos = position
        if (prevMove == Left && !gameState.isWall(pos.x - 1, pos.y)) {
          moveLeft()
        }
        else if (prevMove == Right && !gameState.isWall(pos.x + 1, pos.y)) {
          moveRight()
        }
        else if (prevMove == Up && !gameState.isWall(pos.x, pos.y + 1)) {
          moveUp()
        }
        else if (prevMove == Down && !gameState.isWall(pos.x, pos.y - 1)) {
          moveDown()
        }
        else {
          val legalMoves = Vector(Left, Right, Up, Down).filter { d =>
            d match {
              case Left  => !gameState.isWall(pos.x - 1, pos.y)
              case Right => !gameState.isWall(pos.x + 1, pos.y)
              case Up    => !gameState.isWall(pos.x, pos.y + 1)
              case Down  => !gameState.isWall(pos.x, pos.y - 1)
              case _     => false
            }
          }
          if (legalMoves.size > 0) {
            randomFrom(legalMoves) match {
              case Left  => moveLeft()
              case Right => moveRight()
              case Up    => moveUp()
              case Down  => moveDown()
              case _     =>
            }
          }
        }
      }
    }

    case class Wall(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)
    case class Capsule(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)
    case class Food(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)
    case class Blank(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)

    case class GridCell(x: Int, y: Int, content: Entity)

    case class GameState(grid: Array[Array[GridCell]]) {
      def drawGrid() {
        val pics = grid.flatMap { row =>
          row.map { cell =>
            cell.content.pic
          }
        }
        val cb = canvasBounds
        val gpics = GPics(pics.toList)
        draw(trans(cb.x, cb.y) -> gpics)
      }

      lazy val pacman = {
        val pa = for (
          row <- grid;
          cell <- row if cell.content.isInstanceOf[PacMan]
        ) yield cell.content
        val p = pa(0).asInstanceOf[PacMan]
        p
      }

      lazy val ghosts = for (
        row <- grid;
        cell <- row if cell.content.isInstanceOf[Ghost]
      ) yield {
        val g = cell.content.asInstanceOf[Ghost]
        g
      }

      def isWall(x: Int, y: Int) = grid(y)(x).content.isInstanceOf[Wall]
      def isFood(x: Int, y: Int) = grid(y)(x).content.isInstanceOf[Food]
      def eraseFood(x: Int, y: Int) {
        val food = grid(y)(x).content.asInstanceOf[Food]
        food.pic.erase()
        val blank = Blank(x, y, picMap(' ').apply)
        grid(y)(x) = GridCell(x, y, blank)
        blank.pic.translate(x * tileSize, y * tileSize)
        blank.pic.draw()
      }

      def noFood = {
        val foodcells = for (
          row <- grid;
          cell <- row if cell.content.isInstanceOf[Food]
        ) yield cell
        foodcells.isEmpty
      }
    }

    def loadLayout(layout0: String): GameState = {
      var row = 0
      var col = 0
      val layout: Vector[String] = // a hack to nail down the type to Vector[String] instead of Vector[AnyRef]
        layout0.lines.filter { _.length > 0 }.toArray.toVector.reverse.map(_.asInstanceOf[String])
      val numRows = layout.length
      val numCols = layout(0).length

      val grid = Array.ofDim[GridCell](numRows, numCols)

      layout.foreach { line =>
        line.foreach { c =>
          val pic = picMap(c).apply
          pic.translate(col * tileSize, row * tileSize)
          val entity = c match {
            case 'P' => PacMan(col, row, pic)
            case 'G' => Ghost(col, row, pic)
            case '%' => Wall(col, row, pic)
            case ' ' => Blank(col, row, pic)
            case '.' => Food(col, row, pic)
            case 'o' => Capsule(col, row, pic)
          }
          grid(row)(col) = GridCell(col, row, entity)
          col += 1
        }
        row += 1
        col = 0
      }
      GameState(grid)
    }

    cleari()
    lazy val gameState = loadLayout(layout)
    gameState.drawGrid()
    val pacman = gameState.pacman

    var pendingKey: Option[Int] = None

    timer(20) {
      if (isKeyPressed(Kc.VK_UP)) pendingKey = Some(Kc.VK_UP)
      else if (isKeyPressed(Kc.VK_DOWN)) pendingKey = Some(Kc.VK_DOWN)
      else if (isKeyPressed(Kc.VK_LEFT)) pendingKey = Some(Kc.VK_LEFT)
      else if (isKeyPressed(Kc.VK_RIGHT)) pendingKey = Some(Kc.VK_RIGHT)
    }

    timer(200) {
      val pos = pacman.position
      def upPressed(): Boolean = {
        if (!gameState.isWall(pos.x, pos.y + 1)) {
          pacman.moveUp()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }
      def downPressed(): Boolean = {
        if (!gameState.isWall(pos.x, pos.y - 1)) {
          pacman.moveDown()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }
      def leftPressed(): Boolean = {
        if (!gameState.isWall(pos.x - 1, pos.y)) {
          pacman.moveLeft()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }
      def rightPressed(): Boolean = {
        if (!gameState.isWall(pos.x + 1, pos.y)) {
          pacman.moveRight()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }

      if (pendingKey.isDefined) {
        pendingKey.get match {
          case Kc.VK_UP =>
            if (!upPressed()) {
              pacman.moveContinue()
            }
          case Kc.VK_DOWN =>
            if (!downPressed()) {
              pacman.moveContinue()
            }
          case Kc.VK_LEFT =>
            if (!leftPressed()) {
              pacman.moveContinue()
            }
          case Kc.VK_RIGHT =>
            if (!rightPressed()) {
              pacman.moveContinue()
            }
        }
        pendingKey = None
      }

      if (gameState.noFood) {
        stopAnimation()
        pacman.pic.setFillColor(green)
      }
      gameState.ghosts.foreach { g =>
        if (g.position == pacman.position) {
          stopAnimation()
          pacman.pic.setFillColor(black)
        }
        else {
          g.moveRandom()
          if (g.position == pacman.position) {
            stopAnimation()
            pacman.pic.setFillColor(black)
          }
        }
      }
    }
    activateCanvas()
  }

  def pacmanOrig(): Unit = {
    import kojo.RepeatCommands._
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    // todo
    // pic moveToFront
    // pic copy
    // turtle saveStyle/restoreStyle
    // Picture.circle
    // drawing within animation callbacks?

    val tileSize = 32

    //    def circ(r: Double) {
    //      right(90)
    //      hop(r)
    //      left(90)
    //      circle(r)
    //    }

    def centeredCirc(r: Double, fill: Color, boundary: Color) =
      fillColor(fill) * penColor(boundary) * trans(tileSize / 2, tileSize / 2) -> Picture.circle(r)

    //    def centeredCirc(r: Double, fill: Color, boundary: Color) {
    //      //      saveStyle()
    //      setFillColor(fill)
    //      setPenColor(boundary)
    //      right(90)
    //      hop(tileSize / 2)
    //      left(90)
    //      hop(tileSize / 2)
    //      circ(r)
    //      //      restoreStyle()
    //    }

    def wallPic =
      fillColor(Color(16, 34, 157)) * penColor(blue) -> Picture.rectangle(tileSize, tileSize)

    //    def wallPic = Picture {
    //      setPenColor(blue)
    //      setFillColor(Color(16, 34, 157))
    //      repeat(4) {
    //        forward(tileSize)
    //        right()
    //      }
    //    }

    def foodPic =
      centeredCirc(3, white, white)

    def capsulePic =
      centeredCirc(7, white, yellow)

    def ghostPic =
      centeredCirc(12, Color(255, 16, 42), white)

    def pacmanPic =
      centeredCirc(12, Color(255, 244, 26), black)

    var blankColor = darkBlue
    def blankPic = Picture {
      invisible()
      forward(0)
      //      setFillColor(blankColor)
      //      repeat(4) {
      //        forward(tileSize)
      //        right(90)
      //      }
    }

    val picMap = Map(
      '%' -> wallPic _,
      'o' -> capsulePic _,
      '.' -> foodPic _,
      'G' -> ghostPic _,
      'P' -> pacmanPic _,
      ' ' -> blankPic _
    )

    val layout = """
%%%%%%%%%%%%%%%%%%%%
%o...%........%....%
%.%%.%.%%%%%%.%.%%.%
%.%.......G......%.%
%.%.%%.%%  %%.%%.%.%
%...........%.....P%
%.%.%%.%%%%%%.%%.%.%
%.%...G............%
%.%%.%.%%%%%%.%.%%.%
%....%........%...o%
%%%%%%%%%%%%%%%%%%%%
"""
    case class Position(x: Int, y: Int)
    trait Entity {
      def position: Position
      def pic: Picture
    }
    abstract class DynamicEntity(var position: Position, pic: Picture) extends Entity {
      val Left = 1; val Right = 2; val Up = 3; val Down = 4
      var prevMove = -1
      def moveUp() {
        position = position.copy(y = position.y + 1)
        pic.translate(0, tileSize)
        prevMove = Up
      }
      def moveDown() {
        position = position.copy(y = position.y - 1)
        pic.translate(0, -tileSize)
        prevMove = Down
      }
      def moveLeft() {
        position = position.copy(x = position.x - 1)
        pic.translate(-tileSize, 0)
        prevMove = Left
      }
      def moveRight() {
        position = position.copy(x = position.x + 1)
        pic.translate(tileSize, 0)
        prevMove = Right
      }
    }
    abstract class StaticEntity(val position: Position, pic: Picture) extends Entity

    case class PacMan(x: Int, y: Int, pic: Picture)
      extends DynamicEntity(Position(x, y), pic) {
      def maybeEatFood() {
        val newPos = position
        if (gameState.isFood(newPos.x, newPos.y)) {
          gameState.eraseFood(newPos.x, newPos.y)
        }
      }
      def moveContinue() {
        val pos = position
        if (prevMove == Left && !gameState.isWall(pos.x - 1, pos.y)) {
          moveLeft()
        }
        else if (prevMove == Right && !gameState.isWall(pos.x + 1, pos.y)) {
          moveRight()
        }
        else if (prevMove == Up && !gameState.isWall(pos.x, pos.y + 1)) {
          moveUp()
        }
        else if (prevMove == Down && !gameState.isWall(pos.x, pos.y - 1)) {
          moveDown()
        }
      }
    }
    case class Ghost(x: Int, y: Int, pic: Picture)
      extends DynamicEntity(Position(x, y), pic) {
      def moveRandom() {
        val pos = position
        if (prevMove == Left && !gameState.isWall(pos.x - 1, pos.y)) {
          moveLeft()
        }
        else if (prevMove == Right && !gameState.isWall(pos.x + 1, pos.y)) {
          moveRight()
        }
        else if (prevMove == Up && !gameState.isWall(pos.x, pos.y + 1)) {
          moveUp()
        }
        else if (prevMove == Down && !gameState.isWall(pos.x, pos.y - 1)) {
          moveDown()
        }
        else {
          val legalMoves = Vector(Left, Right, Up, Down).filter { d =>
            d match {
              case Left  => !gameState.isWall(pos.x - 1, pos.y)
              case Right => !gameState.isWall(pos.x + 1, pos.y)
              case Up    => !gameState.isWall(pos.x, pos.y + 1)
              case Down  => !gameState.isWall(pos.x, pos.y - 1)
              case _     => false
            }
          }
          if (legalMoves.size > 0) {
            randomFrom(legalMoves) match {
              case Left  => moveLeft()
              case Right => moveRight()
              case Up    => moveUp()
              case Down  => moveDown()
              case _     =>
            }
          }
        }
      }
    }

    case class Wall(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)
    case class Capsule(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)
    case class Food(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)
    case class Blank(x: Int, y: Int, pic: Picture) extends StaticEntity(Position(x, y), pic)

    case class GridCell(x: Int, y: Int, content: Entity)

    case class GameState(grid: Array[Array[GridCell]]) {
      def drawGrid() {
        val pics = grid.flatMap { row =>
          row.map { cell =>
            cell.content.pic
          }
        }
        val cb = canvasBounds
        pics.foreach { pic =>
          draw(trans(cb.x, cb.y) -> pic)
        }
      }

      lazy val pacman = {
        val pa = for (
          row <- grid;
          cell <- row if cell.content.isInstanceOf[PacMan]
        ) yield cell.content
        val p = pa(0).asInstanceOf[PacMan]
        p
      }

      lazy val ghosts = for (
        row <- grid;
        cell <- row if cell.content.isInstanceOf[Ghost]
      ) yield {
        val g = cell.content.asInstanceOf[Ghost]
        g
      }

      def isWall(x: Int, y: Int) = grid(y)(x).content.isInstanceOf[Wall]
      def isFood(x: Int, y: Int) = grid(y)(x).content.isInstanceOf[Food]

      def eraseFood(x: Int, y: Int) {
        val food = grid(y)(x).content.asInstanceOf[Food]
        food.pic.erase()
        //        blankColor = yellow
        val blankPic = picMap(' ').apply
        val blank = Blank(x, y, blankPic)
        grid(y)(x) = GridCell(x, y, blank)
        val cb = canvasBounds
        //        blankPic.translate(cb.x + x * tileSize, cb.y + y * tileSize)
        //        blankPic.draw()
      }

      def noFood = {
        val foodcells = for (
          row <- grid;
          cell <- row if cell.content.isInstanceOf[Food]
        ) yield cell
        foodcells.isEmpty
      }
    }

    def loadLayout(layout0: String): GameState = {
      var row = 0
      var col = 0
      val layout: Vector[String] = // a hack to nail down the type as Vector[String] instead of Vector[AnyRef]
        layout0.lines.filter { _.length > 0 }.toArray.toVector.reverse.map(_.asInstanceOf[String])
      val numRows = layout.length
      val numCols = layout(0).length

      val grid = Array.ofDim[GridCell](numRows, numCols)

      layout.foreach { line =>
        line.foreach { c =>
          val pic = picMap(c).apply
          pic.translate(col * tileSize, row * tileSize)
          val entity = c match {
            case 'P' => PacMan(col, row, pic)
            case 'G' => Ghost(col, row, pic)
            case '%' => Wall(col, row, pic)
            case ' ' => Blank(col, row, pic)
            case '.' => Food(col, row, pic)
            case 'o' => Capsule(col, row, pic)
          }
          grid(row)(col) = GridCell(col, row, entity)
          col += 1
        }
        row += 1
        col = 0
      }
      GameState(grid)
    }

    cleari()
    setBackground(darkGrey)
    lazy val gameState = loadLayout(layout)
    gameState.drawGrid()
    val pacman = gameState.pacman

    var pendingKey: Option[Int] = None

    timer(20) {
      if (isKeyPressed(Kc.VK_UP)) pendingKey = Some(Kc.VK_UP)
      else if (isKeyPressed(Kc.VK_DOWN)) pendingKey = Some(Kc.VK_DOWN)
      else if (isKeyPressed(Kc.VK_LEFT)) pendingKey = Some(Kc.VK_LEFT)
      else if (isKeyPressed(Kc.VK_RIGHT)) pendingKey = Some(Kc.VK_RIGHT)
    }

    timer(200) {
      val pos = pacman.position
      def upPressed(): Boolean = {
        if (!gameState.isWall(pos.x, pos.y + 1)) {
          pacman.moveUp()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }
      def downPressed(): Boolean = {
        if (!gameState.isWall(pos.x, pos.y - 1)) {
          pacman.moveDown()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }
      def leftPressed(): Boolean = {
        if (!gameState.isWall(pos.x - 1, pos.y)) {
          pacman.moveLeft()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }
      def rightPressed(): Boolean = {
        if (!gameState.isWall(pos.x + 1, pos.y)) {
          pacman.moveRight()
          pacman.maybeEatFood()
          true
        }
        else {
          false
        }
      }

      if (pendingKey.isDefined) {
        pendingKey.get match {
          case Kc.VK_UP =>
            if (!upPressed()) {
              pacman.moveContinue()
            }
          case Kc.VK_DOWN =>
            if (!downPressed()) {
              pacman.moveContinue()
            }
          case Kc.VK_LEFT =>
            if (!leftPressed()) {
              pacman.moveContinue()
            }
          case Kc.VK_RIGHT =>
            if (!rightPressed()) {
              pacman.moveContinue()
            }
        }
      }

      if (gameState.noFood) {
        stopAnimation()
        pacman.pic.setFillColor(green)
      }
      gameState.ghosts.foreach { g =>
        if (g.position == pacman.position) {
          stopAnimation()
          pacman.pic.setFillColor(black)
        }
        else {
          g.moveRandom()
          if (g.position == pacman.position) {
            stopAnimation()
            pacman.pic.setFillColor(black)
          }
        }
      }
    }
    activateCanvas()
  }

  def eraseTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic = Picture {
      repeat(4) {
        forward(100)
        right(90)
      }
    }
    draw(pic)
    animate {
      pic.translate(1, 0)
      if (isKeyPressed(Kc.VK_E)) {
        //        stopAnimation()
        pic.erase()
      }
    }
  }

  def drawPicFromAnimationTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.RepeatCommands._
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    def pp = Picture {
      setFillColor(blue)
      repeat(4) {
        forward(20)
        right(90)
      }
    }
    var pic = pp
    draw(pic)
    animate {
      pic.translate(1, 0)
      if (isKeyPressed(Kc.VK_E)) {
        //        stopAnimation()
        pic.erase()
        pic = pp
        draw(pic)
      }
    }
  }

  def turtlePicTimerDrawTest(): Unit = {
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import collection.mutable.HashSet

    import builtins._
    import turtle._

    cleari()
    def bug = Picture.rectangle(100, 100)
    val bugs = HashSet.empty[Picture]
    var cnt = 0
    val cb = canvasBounds
    def bugGen() {
      cnt += 1
      if (cnt < 7) {
        val bugn = bug
        //        bugn.setPosition(cnt * 10, 200 + cnt * 10)
        draw(bugn)
        bugs += bugn
      }
    }

    timer(1000) {
      bugGen()
    }

    var bugVel = Vector2D(0, -5)

    bugGen()
    //    bugGen()
    //    bugGen()
    //    bugGen()
    //    bugGen()
    //    bugGen()

    animate {
      //      bugGen()
      bugs.foreach { b =>
        b.translate(bugVel)
      }
    }
  }

  def bugsGame(): Unit = {
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    def url(s: String) = s
    import collection.mutable.HashSet

    cleari()
    drawStage(ColorMaker.hsl(198, 1.00, 0.86))
    val cb = canvasBounds
    def bug = Picture.image(url("https://s3.amazonaws.com/codecademy-content/courses/learn-phaser/physics/bug_1.png"))
    val platform = Picture.image(url("https://s3.amazonaws.com/codecademy-content/courses/learn-phaser/physics/platform.png"))
    val player = Picture.image(url("https://s3.amazonaws.com/codecademy-content/courses/learn-phaser/physics/codey.png"))
    var score = 0
    var scoreText = "Your score: 0"

    val bugs = HashSet.empty[Picture]

    player.setPosition(0, cb.y + 25)
    player.scale(0.5)
    draw(player)

    platform.setPosition(cb.x, cb.y)
    platform.scale(3, 0.3)
    draw(platform)

    def bugGen() {
      val bugn = bug
      bugn.setPosition(cb.x + random(cb.width.toInt), cb.y + cb.height)
      draw(bugn)
      bugs += bugn
    }

    timer(100) {
      bugGen()
    }

    var bugVel = Vector2D(0, -1)
    val playerSpeed = 7

    animate {
      bugs.foreach { b =>
        b.translate(bugVel)
        bugVel = (bugVel * 1.01).limit(10)
      }

      bugs.foreach { b =>
        if (b.collidesWith(player)) {
          stopAnimation()
          drawCenteredMessage("You Lose", red, 40)
        }
        else if (b.collidesWith(platform)) {
          b.erase()
          bugs -= b
        }
      }

      if (isKeyPressed(Kc.VK_LEFT)) {
        player.translate(-playerSpeed, 0)
      }
      if (isKeyPressed(Kc.VK_RIGHT)) {
        player.translate(playerSpeed, 0)
      }
    }
    showGameTime(10, "You Win", black, 20)
    showFps(black, 15)
    activateCanvas()
  }

  def carGame(): Unit = {
    import kojo.RepeatCommands._
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    // Use the four arrow keys to avoid the blue cars
    // You gain energy every second, and lose energy for every collision
    // You lose if your energy drops below zero, or you hit the edges of the screen
    // You win if you stay alive for a minute
    switchToDefault2Perspective()
    cleari()
    drawStage(black)
    // setRefreshRate(50)

    val cb = canvasBounds
    val carHeight = 100
    val markerHeight = 80
    // The collision polygon for the (very similarly sized) car images car1.png and car2.png
    val carE = trans(2, 14) -> Picture {
      repeat(2) {
        forward(70); right(45); forward(20); right(45)
        forward(18); right(45); forward(20); right(45)
      }
    }
    def car(img: String) = Picture.image(img, carE)

    val cars = collection.mutable.Map.empty[Picture, Vector2D]
    val carSpeed = 3
    val pResponse = 3
    var pVel = Vector2D(0, 0)
    var disabledTime = 0d

    val bplayer = newMp3Player
    val cplayer = newMp3Player

    val urlBase = "https://kojofiles.netlify.app"
    val player = car(url(s"$urlBase/car1.png"))
    def createCar() {
      val c = trans(player.position.x + randomNormalDouble * cb.width / 10, cb.y + cb.height) ->
        car(url(s"$urlBase/car2.png"))
      draw(c)
      cars += c -> Vector2D(0, -carSpeed)
    }
    val markers = collection.mutable.Set.empty[Picture]
    def createMarker() {
      val mwidth = 20
      val m = fillColor(white) * penColor(white) *
        trans(cb.x + cb.width / 2 - mwidth / 2, cb.y + cb.height) -> Picture.rect(markerHeight, mwidth)
      draw(m)
      markers += m
    }
    var energyLevel = 0
    def energyText = s"Energy: $energyLevel"
    val energyLabel = Picture.textu(energyText, 20, ColorMaker.aquamarine)
    energyLabel.translate(cb.x + 10, cb.y + cb.height - 10)

    def drawMessage(m: String, c: Color) {
      drawCenteredMessage(m, c, 30)
    }
    def updateEnergyTick() {
      energyLevel += 2
      energyLabel.update(energyText)
    }
    def updateEnergyCrash() {
      energyLevel -= 10
      energyLabel.update(energyText)
      if (energyLevel < 0) {
        drawMessage("You're out of energy! You Lose", red)
        stopAnimation()
      }
    }
    def manageGameScore() {
      var gameTime = 0
      val timeLabel = Picture.textu(gameTime, 20, ColorMaker.azure)
      timeLabel.translate(cb.x + 10, cb.y + 50)
      draw(timeLabel)
      draw(energyLabel)
      timeLabel.forwardInputTo(stageArea)

      timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)
        updateEnergyTick()

        if (gameTime == 60) {
          drawMessage("Time up! You Win", green)
          stopAnimation()
        }
      }
    }

    draw(player)
    drawAndHide(carE)

    timer(1000) {
      createMarker()
      createCar()
    }

    animate {
      player.moveToFront()
      val enabled = epochTimeMillis - disabledTime > 300
      if (enabled) {
        if (isKeyPressed(Kc.VK_LEFT)) {
          pVel = Vector2D(-pResponse, 0)
          player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_RIGHT)) {
          pVel = Vector2D(pResponse, 0)
          player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_UP)) {
          pVel = Vector2D(0, pResponse)
          player.translate(pVel)
          if (!isMp3Playing) {
            playMp3Sound("/media/car-ride/car-accel.mp3")
          }
        }
        else {
          stopMp3()
        }
        if (isKeyPressed(Kc.VK_DOWN)) {
          pVel = Vector2D(0, -pResponse)
          player.translate(pVel)
          if (!bplayer.isMp3Playing) {
            bplayer.playMp3Sound("/media/car-ride/car-brake.mp3")
          }
        }
        else {
          bplayer.stopMp3()
        }
      }
      else {
        player.translate(pVel)
      }

      if (player.collidesWith(stageLeft) || player.collidesWith(stageRight)) {
        cplayer.playMp3Sound("/media/car-ride/car-crash.mp3")
        player.setOpacity(0.5)
        drawMessage("You Crashed!", red)
        stopAnimation()
      }
      else if (player.collidesWith(stageTop)) {
        pVel = Vector2D(0, -pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
      }
      else if (player.collidesWith(stageBot)) {
        pVel = Vector2D(0, pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
      }

      cars.foreach { cv =>
        val (c, vel) = cv
        c.moveToFront()
        if (player.collidesWith(c)) {
          cplayer.playMp3Sound("/media/car-ride/car-crash.mp3")
          pVel = bouncePicVectorOffPic(player, pVel - vel, c) / 2
          player.translate(pVel * 3)
          c.translate(-pVel * 3)
          disabledTime = epochTimeMillis
          updateEnergyCrash()
        }
        else {
          val newVel = Vector2D(vel.x + randomDouble(1) / 2 - 0.25, vel.y)
          cars += c -> newVel
          c.translate(newVel)
        }
        if (c.position.y + carHeight < cb.y) {
          c.erase()
          cars -= c
        }
      }
      markers.foreach { m =>
        m.translate(0, -carSpeed * 2)
        if (m.position.y + markerHeight < cb.y) {
          m.erase()
          markers -= m
        }
      }
    }

    manageGameScore()
    playMp3Loop("/media/car-ride/car-move.mp3")
    activateCanvas()

    // Car images, via google images, from http://motor-kid.com/race-cars-top-view.html
    // and www.carinfopic.com
    // Car sounds from http://soundbible.com

  }

  def carGame2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    // Use the four arrow keys to avoid the blue cars
    // You gain energy every second, and lose energy for every collision
    // You lose if your energy drops below zero, or you hit the edges of the screen
    // You win if you stay alive for a minute
    cleari()
    drawStage(black)

    val cb = canvasBounds
    val carHeight = 100
    val markerHeight = 80
    // The collision polygon for the (very similarly sized) car images car1.png and car2.png
    val carE = trans(2, 14) -> Picture {
      repeat(2) {
        forward(70); right(45); forward(20); right(45)
        forward(18); right(45); forward(20); right(45)
      }
    }
    def car(img: String) = Picture.image(url(img), carE)

    val cars = collection.mutable.Map.empty[Picture, Vector2D]
    val carSpeed = 3
    val pResponse = 3
    var pVel = Vector2D(0, 0)
    var disabledTime = 0.0

    val bplayer = newMp3Player
    val cplayer = newMp3Player

    val urlBase = "https://kojofiles.netlify.app"
    val player = car(s"$urlBase/car1.png")

    def createCar() {
      val c = trans(player.position.x + randomNormalDouble * cb.width / 10, cb.y + cb.height) ->
        car(s"$urlBase/car2.png")
      draw(c)
      cars += c -> Vector2D(0, -carSpeed)
    }
    val markers = collection.mutable.Set.empty[Picture]
    def createMarker() {
      val mwidth = 20
      val m = fillColor(white) * penColor(white) *
        trans(cb.x + cb.width / 2 - mwidth / 2, cb.y + cb.height) -> Picture.rect(markerHeight, mwidth)
      draw(m)
      markers += m
    }

    draw(player)
    drawAndHide(carE)

    timer(1000) {
      createMarker()
      createCar()
    }

    var energyLevel = 0
    def energyText = s"Energy: $energyLevel"
    val energyLabel = Picture.textu(energyText, 20, ColorMaker.aquamarine)
    energyLabel.translate(cb.x + 10, cb.y + cb.height - 10)

    def drawMessage(m: String, c: Color) {
      drawCenteredMessage(m, c, 30)
    }
    def updateEnergyCrash() {
      energyLevel -= 10
      energyLabel.update(energyText)
      if (energyLevel < 0) {
        drawMessage("You're out of energy! You Lose", red)
        stopAnimation()
      }
    }

    animate {
      player.moveToFront()
      val enabled = epochTimeMillis - disabledTime > 300
      if (enabled) {
        if (isKeyPressed(Kc.VK_LEFT)) {
          pVel = Vector2D(-pResponse, 0)
          player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_RIGHT)) {
          pVel = Vector2D(pResponse, 0)
          player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_UP)) {
          pVel = Vector2D(0, pResponse)
          player.translate(pVel)
          if (!isMp3Playing) {
            playMp3Sound("/media/car-ride/car-accel.mp3")
          }
        }
        else {
          stopMp3()
        }
        if (isKeyPressed(Kc.VK_DOWN)) {
          pVel = Vector2D(0, -pResponse)
          player.translate(pVel)
          if (!bplayer.isMp3Playing) {
            bplayer.playMp3Sound("/media/car-ride/car-brake.mp3")
          }
        }
        else {
          bplayer.stopMp3()
        }
      }
      else {
        player.translate(pVel)
      }

      if (player.collidesWith(stageLeft) || player.collidesWith(stageRight)) {
        cplayer.playMp3Sound("/media/car-ride/car-crash.mp3")
        player.setOpacity(0.5)
        drawMessage("You Crashed!", red)
        stopAnimation()
      }
      else if (player.collidesWith(stageTop)) {
        pVel = Vector2D(0, -pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
      }
      else if (player.collidesWith(stageBot)) {
        pVel = Vector2D(0, pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
      }

      cars.foreach { cv =>
        val (c, vel) = cv
        c.moveToFront()
        if (player.collidesWith(c)) {
          cplayer.playMp3Sound("/media/car-ride/car-crash.mp3")
          pVel = bouncePicVectorOffPic(player, pVel - vel, c) / 2
          player.translate(pVel * 3)
          c.translate(-pVel * 3)
          disabledTime = epochTimeMillis
          updateEnergyCrash()
        }
        else {
          val newVel = Vector2D(vel.x + randomDouble(1) / 2 - 0.25, vel.y)
          cars += c -> newVel
          c.translate(newVel)
        }
        if (c.position.y + carHeight < cb.y) {
          c.erase()
          cars -= c
        }
      }
      markers.foreach { m =>
        m.translate(0, -carSpeed * 2)
        if (m.position.y + markerHeight < cb.y) {
          m.erase()
          markers -= m
        }
      }
    }

    def updateEnergyTick() {
      energyLevel += 2
      energyLabel.update(energyText)
    }
    def manageGameScore() {
      var gameTime = 0
      val timeLabel = Picture.textu(gameTime, 20, ColorMaker.azure)
      timeLabel.translate(cb.x + 10, cb.y + 50)
      draw(timeLabel)
      draw(energyLabel)
      timeLabel.forwardInputTo(stageArea)

      timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)
        updateEnergyTick()

        if (gameTime == 60) {
          drawMessage("Time up! You Win", green)
          stopAnimation()
        }
      }
    }

    manageGameScore()
    playMp3Loop("/media/car-ride/car-move.mp3")
    activateCanvas()

    // Car images, via google images, from http://motor-kid.com/race-cars-top-view.html
    // and www.carinfopic.com
  }

  def picInvisibleTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic = Picture.rectangle(100, 50)
    draw(pic)

    timer(1000) {
      pic.invisible()
      stopAnimation()
    }
  }

  def picMoveToFrontTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    val pic2 = fillColor(blue) * trans(-20, 0) -> Picture.rectangle(100, 50)
    draw(pic1, pic2)

    var backPic = pic1
    timer(1000) {
      backPic.moveToFront()
      if (backPic == pic1) {
        backPic = pic2
      }
      else {
        backPic = pic1
      }
    }
  }

  def picMoveToBackTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    val pic2 = fillColor(blue) * trans(-20, 0) -> Picture.rectangle(100, 50)
    draw(pic1, pic2)

    var frontPic = pic2
    timer(1000) {
      frontPic.moveToBack()
      if (frontPic == pic1) {
        frontPic = pic2
      }
      else {
        frontPic = pic1
      }
    }
  }

  def picMousePressTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    pic1.onMousePress { (x, y) =>
      pic1.setPosition(x, y)
    }
    draw(pic1)
  }

  def picMouseClickTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    pic1.onMouseClick { (x, y) =>
      pic1.setPosition(x, y)
    }
    draw(pic1)
  }

  def picInsideHPicsMousePressTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    showAxes()
    showGrid()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    val pic2 = fillColor(blue) -> Picture.rectangle(100, 50)
    val pic = trans(100, 100) -> HPics(pic2, pic1)
    pic1.onMousePress { (x, y) =>
      println(x, y)
      pic1.setPosition(x, y)
    }
    draw(pic)
  }

  def picMouseDragTest(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    draw(pic1)
    pic1.onMouseRelease { (x, y) =>
      println(s"P1 Release: $x, $y")
    }
    pic1.onMouseDrag { (x, y) =>
      println(s"P1 Drag: $x, $y")
    }
  }

  def picMouseEventTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic1 = fillColor(green) -> Picture.rectangle(100, 50)
    val pic2 = fillColor(blue) -> Picture.rectangle(50, 100)
    pic1.onMousePress { (x, y) =>
      println(s"P1 Press: $x, $y")
    }
    pic1.onMouseRelease { (x, y) =>
      println(s"P1 Release: $x, $y")
    }
    pic1.onMouseDrag { (x, y) =>
      println(s"P1 Drag: $x, $y")
    }
    pic1.onMouseClick { (x, y) =>
      println(s"Click: $x, $y")
    }
    pic1.onMouseMove { (x, y) =>
      println(s"Move: $x, $y")
    }
    pic1.onMouseEnter { (x, y) =>
      println(s"Enter: $x, $y")
    }
    pic1.onMouseExit { (x, y) =>
      println(s"Exit: $x, $y")
    }

    pic2.onMousePress { (x, y) =>
      println(s"P2 Press: $x, $y")
    }
    pic2.onMouseRelease { (x, y) =>
      println(s"P2 Release: $x, $y")
    }
    pic2.onMouseDrag { (x, y) =>
      println(s"P2 Drag: $x, $y")
    }

    draw(pic1, pic2)
  }

  def collidiumGame(): Unit = {
    import kojo.doodle.Color
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Picture
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    // Sling the ball (with the mouse) towards the target on the top-right.
    // Then draw paddles on the canvas (with the mouse) to guide the ball
    // away from the obstacles and towards the target.
    // You win if you hit the target within a minute
    switchToDefault2Perspective()
    cleari()
    // setRefreshRate(50)

    drawStage(darkGray)
    val cb = canvasBounds
    val obsDelta = cb.width / 4
    val ballDeltaBase = (obsDelta / 4).toInt
    def ballDelta = ballDeltaBase + random(ballDeltaBase)
    val ballSize = 20

    val urlBase = "https://kojofiles.netlify.app"
    val ballE = penColor(red) * trans(ballSize, ballSize) -> Picture.circle(ballSize)
    val ball1 = Picture.image(s"$urlBase/collidium/ball1.png", ballE)
    val ball2 = Picture.image(s"$urlBase/collidium/ball2.png", ballE)
    val ball3 = Picture.image(s"$urlBase/collidium/ball3.png", ballE)
    val ball4 = Picture.image(s"$urlBase/collidium/ball4.png", ballE)

    val ball = picBatch(ball1, ball2, ball3, ball4)
    ball.translate(cb.x + ballDelta, cb.y + ballDelta)

    val target = trans(-cb.x - ballDelta, -cb.y - ballDelta) *
      penColor(red) *
      fillColor(red) -> PicShape.circle(ballSize / 4)

    val obstacles = (1 to 3).map { n =>
      trans(cb.x + n * obsDelta, cb.y + cb.height / 4) * fillColor(green) * penColor(noColor) -> PicShape.rect(cb.height / 2, 12)
    }

    draw(ball, target)
    obstacles.foreach { o => draw(o) }
    playMp3Sound("/media/collidium/hit.mp3")

    def drawMessage(m: String, c: Color) {
      val te = textExtent(m, 30)
      val pic = penColor(c) * trans(cb.x + (cb.width - te.width) / 2, 0) -> PicShape.text(m, 30)
      draw(pic)
    }

    def manageGameTime() {
      var gameTime = 0
      val timeLabel = PicShape.textu(gameTime, 20, blue)
      timeLabel.translate(cb.x + 10, cb.y + 50)
      draw(timeLabel)
      timeLabel.forwardInputTo(stageArea)

      timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)

        if (gameTime == 60) {
          drawMessage("Time up! You Lose", red)
          stopAnimation()
        }
      }
    }

    import collection.mutable.ArrayBuffer
    def line(ps: ArrayBuffer[Point], c: Color) = penColor(c) -> Picture.fromPath { path =>
      path.moveTo(ps(0).x, ps(0).y)
      path.lineTo(ps(1).x, ps(1).y)
    }
    val slingPts = ArrayBuffer.empty[Point]
    var sling: Picture = PicShape.hline(1)
    var paddle: Picture = PicShape.hline(1)
    var tempPaddle = paddle
    drawAndHide(paddle)
    var slingMode = false
    ball.onMousePress { (x, y) =>
      slingMode = true
      slingPts.clear()
      slingPts += Point(ball.position.x, ball.position.y)
    }

    ball.onMouseDrag { (x, y) =>
      if (slingPts.size > 1) {
        slingPts.remove(1)
      }
      slingPts += Point(x, y)
      sling.erase()
      sling = line(slingPts, green)
      sling.draw()
      println("drag")
    }

    def ballOnMouseRelease(x: Double, y: Double) {
      println("release")
      sling.erase()
      ball.forwardInputTo(stageArea)
      var vel = if (slingPts.size == 1)
        Vector2D(1, 1)
      else
        Vector2D(slingPts(0).x - slingPts(1).x, slingPts(0).y - slingPts(1).y).limit(7)
      println(s"Launch velocity: $vel")
      animate {
        ball.translate(vel)
        ball.showNext()
        if (ball.collidesWith(stageBorder)) {
          playMp3Sound("/media/collidium/hit.mp3")
          vel = bouncePicVectorOffStage(ball, vel)
        }
        else if (ball.collidesWith(paddle)) {
          playMp3Sound("/media/collidium/hit.mp3")
          vel = bouncePicVectorOffPic(ball, vel, paddle)
          ball.translate(vel)
        }
        else if (ball.collidesWith(target)) {
          target.setPenColor(green)
          target.setFillColor(green)
          drawMessage("Yaay! You Win", green)
          stopAnimation()
          playMp3Sound("/media/collidium/win.mp3")
        }

        ball.collision(obstacles) match {
          case Some(obstacle) =>
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffPic(ball, vel, obstacle)
            while (ball.collidesWith(obstacle)) {
              ball.translate(vel)
            }
          case None =>
        }

      }
      manageGameTime()
    }

    val paddlePts = ArrayBuffer.empty[Point]
    stageArea.onMousePress { (x, y) =>
      paddle.erase()
      paddlePts.clear()
      paddlePts += Point(x, y)
    }

    stageArea.onMouseDrag { (x, y) =>
      if (paddlePts.size > 1) {
        paddlePts.remove(1)
      }
      paddlePts += Point(x, y)
      tempPaddle.erase()
      tempPaddle = line(paddlePts, ColorMaker.aquamarine)
      tempPaddle.draw()
      tempPaddle.onMouseRelease { (x, y) =>
        if (tempPaddle.collidesWith(ball)) {
          tempPaddle.erase()
        }
        else {
          paddle = tempPaddle
          paddle.setPenColor(yellow)
          //        paddle.setFillColor(yellow)
          paddle.forwardInputTo(stageArea)
        }
      }
    }

    stageArea.onMouseRelease { (x, y) =>
      if (slingMode) {
        slingMode = false
        ballOnMouseRelease(x, y)
      }
      else {
        if (tempPaddle.collidesWith(ball)) {
          tempPaddle.erase()
        }
        else {
          paddle = tempPaddle
          paddle.setPenColor(yellow)
          //        paddle.setFillColor(yellow)
          paddle.forwardInputTo(stageArea)
        }
      }
    }

    target.forwardInputTo(stageArea)
    obstacles.foreach { o => o.forwardInputTo(stageArea) }

    // Game idea and sounds from https://github.com/shadaj/collidium
  }

  def picGeomExtra00Test(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import com.vividsolutions.jts.geom.LineString

    import builtins._
    import turtle._
    cleari()
    val pic = Picture {
      setPosition(-50, -50)
      moveTo(-50, 50)
    }

    draw(pic)
    animate {
      if (pic.made) {
        val bounds = pic.tnode.getLocalBounds
        val geom = pic.picGeom
        println(bounds.x, bounds.y, bounds.width, bounds.height)
        println(LineString.asString(geom.asInstanceOf[LineString]))
        stopAnimation()
      }
    }
  }

  def picColorChangeTest(): Unit = {
    import kojo.KojoWorldImpl
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic = Picture.rectangle(100, 50)
    var color = red
    draw(pic)

    animate {
      pic.setPenColor(color)
      color = color.spin(10)
      pic.rotate(1)
    }
  }

  def pathPicTest(): Unit = {
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    drawStage(darkGray)
    val pic = Picture.fromPath { g =>
      g.moveTo(0, 0)
      g.lineTo(-100, -100)
      g.lineTo(-100, 100)
      //      g.quadraticCurveTo(100, -50, 0, 0)
      g.lineTo(100, 100)
      g.quadraticCurveTo(150, 50, 100, 0)
    }
    pic.setPenColor(blue)
    pic.setFillColor(green)
    draw(pic)
    var velocity = Vector2D(4, 5)
    animate {
      pic.translate(velocity)
      if (pic.collidesWith(stageBorder)) {
        velocity = bouncePicVectorOffStage(pic, velocity)
      }
    }
    activateCanvas()
  }

  def physics1(): Unit = {
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    drawStage(darkGray)
    val cb = canvasBounds
    val body = Picture.rectangle(100, 60)
    body.setPosition(cb.x + 150, 200)
    val platform1 = Picture.rectangle(cb.width / 2, 40)
    val platform2 = Picture.rectangle(cb.width / 2, 40)
    platform2.setPosition(cb.x, cb.y + 30)
    val platforms = Seq(platform1, platform2)
    draw(body, platform1, platform2)

    val gravity = Vector2D(0, -0.5)
    var vel = Vector2D(14, 0)
    val bounceFactor = 0.2

    animate {
      body.translate(vel)
      val prevVel = vel
      if (body.collidesWith(stageBorder)) {
        vel = bouncePicVectorOffStage(body, vel) * bounceFactor
      }

      platforms.foreach { platform =>
        if (body.collidesWith(platform)) {
          vel = bouncePicVectorOffPic(body, vel, platform) * bounceFactor
        }
      }

      vel = vel + gravity
      if (body.collidesWith(stageBot)) {
        if (vel.y < 0) {
          vel = Vector2D(vel.x, 0)
        }
      }
      platforms.foreach { platform =>
        println("---")
        println(vel)
        println(prevVel)
        if (body.collidesWith(platform)) {
          println("Collision with platform")
          if (prevVel.y <= 0 && vel.y < 0) {
            vel = Vector2D(vel.x, 0)
            println("Setting vel to zero")
          }
        }
      }
    }
  }

  def rotateAboutPoint(): Unit = {
    import kojo.doodle.Color._
    import kojo.syntax.Builtins
    import kojo.KojoWorldImpl
    import kojo.Vector2D
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._

    cleari()
    val pic = Picture.rectangle(100, 100)
    val pic2 = Picture.rectangle(50, 50)
    draw(pic, pic2)
    pic.rotateAboutPoint(45, 50, 50)
  }

  def pentagonPattern(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    clear()
    setSpeed(fast)
    var clr = ColorMaker.hsla(0, 1.00, 0.5, 0.36)
    setPenColor(black)
    repeat(18) {
      setFillColor(clr)
      repeat(5) {
        forward(100)
        right(72)
      }
      right(20)
      clr = clr.spin(20)
    }
  }

  def manyForwards(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    def shape() {
      savePosHe()
      forward(100)
      right(90)
      forward(10)
      right(75)
      right(909)
      right(65)
      forward(50)
      right()
      forward(50)
      restorePosHe()
    }

    def block() {
      shape()
      right(20)
    }
    clear()
    setBackground(white)
    setSlowness(0)
    repeat(1000) {
      block()
    }
    right(160)
    invisible()
  }

  def hpics1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    clearOutput()
    drawStage(white)
    val pic1 = Picture.rectangle(100, 50)
    val pic2 = rot(45) -> Picture.rectangle(50, 100)
    val pic3 = Picture.rectangle(100, 50)
    val pic = rot(30) -> HPics(pic1, pic2, pic3)
    val pic4 = Picture.circle(30)
    val pic5 = HPics(pic4, pic)
    draw(pic5)

    var vel = Vector2D(2, 3)
    animate {
      pic5.translate(vel)
      if (pic5.collidesWith(stageBorder)) {
        vel = bouncePicOffStage(pic5, vel)
      }
    }
  }

  def hpicsFlower(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    def flower = Picture {
      def shape() {
        savePosHe()
        left(45)
        right(90, 100)
        right(90)
        right(90, 100)
        restorePosHe()
      }

      def block() {
        shape()
        right(18)
      }

      setPenColor(cm.silver)
      repeat(20) {
        block()
      }
    }

    cleari()
    setBackground(cm.rgb(40, 40, 40))
    val pic1 = flower
    val pic2 = flower
    val pic = HPics(pic1, pic2)
    draw(pic)
  }

  def hpics2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    drawStage(white)
    val pic1 = Picture.rectangle(100, 50)
    val pic2 = Picture.rectangle(50, 100)
    val pic = HPics(pic1, pic2)
    draw(pic)

    var vel = Vector2D(2, 3)
    animate {
      pic.translate(vel)
      if (pic.collidesWith(stageBorder)) {
        vel = bouncePicOffStage(pic, vel)
      }
    }
  }

  def rotatedPic(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    val angle = 45
    val w = 50
    val h = 50
    val p = Picture.rectangle(w, h)
    p.draw()
    p.rotate(angle)
    Utils.printRectangle(p.bounds)
    Utils.printRectangle(p.tnode.getBounds)
    Utils.printRectangle(p.tnode.getLocalBounds)
  }

  def hpicsCentered1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    clearOutput()
    drawStage(white)
    val pic1 = Picture.rectangle(100, 50)
    val pic2 = rot(45) -> Picture.rectangle(50, 100)
    val pic3 = Picture.rectangle(100, 50)
    val pic = HPics2(pic1, pic2, pic3)
    draw(pic)
  }

  def hpicsCentered2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    clearOutput()
    drawStage(white)
    val pic1 = Picture.rectangle(100, 50)
    val pic2 = rot(45) -> Picture.rectangle(50, 100)
    val pic3 = Picture.rectangle(100, 50)
    val pic = rot(30) -> HPics2(pic1, pic2, pic3)
    val pic4 = Picture.circle(30)
    val pic5 = HPics2(pic4, pic)
    draw(pic5)
  }

  def longDrawPic(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    def flower = Picture {
      def shape() {
        savePosHe()
        forward(100)
        right(90)
        forward(10)
        right(75)
        right(909)
        right(65)
        forward(50)
        right()
        forward(50)
        restorePosHe()
      }

      def block() {
        setPenColor(randomColor.fadeOut(0.5))
        shape()
        right(20)
      }
      repeat(600) {
        block()
      }
    }

    cleari()
    setRandomSeed(42)
    setBackground(cm.rgb(40, 40, 40))
    val pic1 = flower
    draw(pic1)
  }

  def hpicsDelayed(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    setBackground(white)
    val p1 = Picture.rectangle(100, 50)
    val p2 = Picture.text("Hello World", 20)
    val p3 = Picture.circle(25)
    val p4 = Picture {
      left(45)
      right(90, 100)
      right(90)
      right(90, 100)
    }
    val pic = HPics2(rot(10) -> p1, rot(-20) -> p2, scale(0.5) -> p3, rot(30) -> p4)
    draw(pic)
  }

  def hpicsDelayed2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    setBackground(white)
    val p1 = Picture.rectangle(100, 50)
    //    val p2 = Picture.text("Hello World", 20)
    val p3 = Picture.circle(25)
    val p4 = Picture {
      def shape() {
        savePosHe()
        forward(100)
        right(90)
        forward(10)
        right(75)
        right(909)
        right(65)
        forward(50)
        right()
        forward(50)
        restorePosHe()
      }

      def block() {
        setPenColor(randomColor.fadeOut(0.5))
        shape()
        right(20)
      }
      repeat(600) {
        block()
      }
    }
    val pic = HPics(rot(10) -> p1, rot(30) -> p4, scale(1.5) -> p3)
    draw(pic)
  }

  def vertexShape1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val pic = Picture.fromVertexShape { s =>
      s.beginShape()
      s.vertex(0, 100)
      s.vertex(100, 200)
      s.vertex(200, 0)
      s.vertex(0, 100)
      s.endShape()
    }
    draw(pic)
  }

  def vertexShape2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    def pic(r: Double) = Picture.fromVertexShape { s =>
      import s._
      beginShape()
      curveVertexRt(r, 90 - 15)
      curveVertexRt(r, 90 - 15)

      curveVertexRt(r * 1.2, 90)

      curveVertexRt(r, 90 + 15)
      curveVertexRt(r, 90 + 15)
      endShape()
    }
    draw(pic(200))
  }

  def mandala1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    // needs Kojo v2.9.03
    cleari()
    setBackground(cm.rgb(10, 10, 10))

    def centralFlower(stroke: Color, fill: Color) = Picture {
      right(90)
      setPenColor(stroke)
      setFillColor(fill)
      left(45)
      right(90, 100)
      right(90)
      right(90, 100)
    }

    def lotusPetal(stroke: Color, fill: Color, rad: Double, radExtent: Double,
                   theta: Double, thetaExtent: Double) =
      penColor(stroke) * fillColor(fill) -> Picture.fromVertexShape { p =>
        val tDelta = thetaExtent / 2
        p.beginShape()
        p.curveVertexRt(rad, theta - tDelta)
        p.curveVertexRt(rad, theta - tDelta)

        val rExtent = radExtent / rad

        p.curveVertexRt(mathx.lerp(rad, rad * rExtent, 0.6), theta - tDelta * 22 / 30)
        p.curveVertexRt(mathx.lerp(rad, rad * rExtent, 0.7), theta - tDelta * 2 / 30)
        p.curveVertexRt(rad * rExtent, theta)
        p.curveVertexRt(mathx.lerp(rad, rad * rExtent, 0.7), theta + tDelta * 2 / 30)
        p.curveVertexRt(mathx.lerp(rad, rad * rExtent, 0.6), theta + tDelta * 22 / 30)

        p.curveVertexRt(rad, theta + tDelta)
        p.curveVertexRt(rad, theta + tDelta)
        p.endShape()
      }

    def diya(stroke: Color, fill: Color, rad: Double, radExtent: Double,
             theta: Double, thetaExtent: Double) =
      penColor(stroke) * fillColor(fill) -> Picture.fromVertexShape { p =>
        val tDelta = thetaExtent / 2
        val rExtent = radExtent / rad
        p.beginShape()
        p.curveVertexRt(rad, theta)
        p.curveVertexRt(rad, theta)
        p.curveVertexRt(mathx.lerp(rad, rad * rExtent, 0.5), theta - tDelta / 4)
        p.curveVertexRt(rad * rExtent, theta)
        p.curveVertexRt(rad * rExtent, theta)
        p.endShape()

        p.beginShape()
        p.curveVertexRt(rad * rExtent, theta)
        p.curveVertexRt(rad * rExtent, theta)
        p.curveVertexRt(mathx.lerp(rad, rad * rExtent, 0.5), theta + tDelta / 4)
        p.curveVertexRt(rad, theta)
        p.curveVertexRt(rad, theta)
        p.endShape()
      }

    def inscribedTriangle(vertexR: Double, vertexTheta: Double) = Picture.fromVertexShape { s =>
      import s._
      beginShape()
      vertexRt(vertexR, vertexTheta)
      vertexRt(vertexR, vertexTheta + 120)
      vertexRt(vertexR, vertexTheta + 240)
      vertexRt(vertexR, vertexTheta)
      endShape()
    }

    def inscribedSquare(vertexR: Double, vertexTheta: Double) = Picture.fromVertexShape { s =>
      import s._
      beginShape()
      vertexRt(vertexR, vertexTheta)
      vertexRt(vertexR, vertexTheta + 90)
      vertexRt(vertexR, vertexTheta + 180)
      vertexRt(vertexR, vertexTheta + 270)
      vertexRt(vertexR, vertexTheta)
      endShape()
    }

    def altar(r: Double, gateRFraction: Double, direction: Double) = Picture {
      setHeading(direction)
      hop(r)
      left(90)
      val glen = r * gateRFraction
      hop(glen)
      repeat(4) {
        forward(r - glen)
        left()
        forward(r - glen)
        right()
        forward(r / 10)
        right()
        forward(r / 4)
        left()
        forward(r / 10)
        left()
        forward(r / 4)
        forward(glen * 2)
        forward(r / 4)
        left()
        forward(r / 10)
        left()
        forward(r / 4)
        right()
        forward(r / 10)
        right(90)
      }
    }

    val pics = ArrayBuffer.empty[Picture]

    pics.append(penColor(white) * fillColor(black.fadeOut(0.8)) -> inscribedTriangle(80, 90))
    pics.append(penColor(white) * fillColor(black.fadeOut(0.8)) -> inscribedTriangle(80, -90))

    repeatFor(0 to 11) { n =>
      val pic1 = rot(n * 30) -> centralFlower(white, cm.lightBlue)
      pics.append(pic1)
    }

    pics.append(penColor(white) * fillColor(black) -> Picture.circle(145))

    repeatFor(0 to 5) { n =>
      val pic2 = lotusPetal(white, ColorMaker.hsl(0, 0.86, 0.66), 145, 180, n * 60, 60)
      pics.append(pic2)
    }

    pics.append(penColor(white) -> Picture.circle(190))

    repeatFor(0 to 11) { n =>
      val pic2 = diya(white, ColorMaker.hsl(27, 0.86, 0.66), 200, 250, n * 30, 30)
      pics.append(pic2)
    }

    pics.append(penColor(white) * penWidth(10) -> Picture.circle(260))

    repeatFor(0 to 11) { n =>
      val pic2 = lotusPetal(white, ColorMaker.hsl(0, 0.86, 0.66), 270, 290, n * 30, 30)
      pics.append(pic2)
    }

    pics.append(penColor(white) * penWidth(5) -> Picture.circle(300))

    repeatFor(0 to 23) { n =>
      val pic2 = diya(white, ColorMaker.hsl(27, 0.86, 0.66), 305, 360, n * 15 + 7.5, 15)
      pics.append(pic2)
    }

    pics.append(penColor(white) * penWidth(5) -> altar(370, 0.2, 0))

    //draw(pics)
    draw(pics.reverse)
  }

  def curveVertex1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    clear()
    setSpeed(superFast)
    beginShape()
    curveVertex(50, 50)
    curveVertex(50, 50)

    curveVertex(100, 150)

    curveVertex(150, 50)
    curveVertex(150, 50)
    endShape()
  }

  def size1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    clear()
    size(400, 400)
    setBackground(black)
    drawStage(green)
    println(canvasBounds)
    println(cwidth)
    println(cheight)

    repeat(4) {
      forward(300)
      right(90)
    }
  }

  def grid1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    size(600, 600)
    //    originAt(300, 400)
    zoomXY(-2, -2, 100, 100)
    cleari()
    setSpeed(superFast)
    setBackground(blue)
    setPenColor(black)

    val tileCount = 10
    val tileSize = cwidth / tileCount

    def shape() {
      repeat(4) {
        forward(tileSize)
        right(90)
      }
    }

    def block(posX: Double, posY: Double) {
      setPosition(posX, posY)
      shape()
    }

    repeatFor(rangeTill(0, cheight, tileSize)) { posY =>
      repeatFor(rangeTill(0, cwidth, tileSize)) { posX =>
        block(posX, posY)
      }
    }
  }

  def dynamicGrid1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    size(600, 600)
    cleari()
    setBackground(white)
    originBottomLeft()

    val tileCount = 10
    val tileWidth = cwidth / tileCount
    val tileHeight = cheight / tileCount

    def shape = Picture.rectangle(tileWidth, tileHeight)

    def block(posX: Double, posY: Double) {
      val pic = shape
      pic.setPosition(posX, posY)
      pic.setPenColor(cm.darkBlue)
      val d = mathx.distance(posX, posY, mouseX, mouseY)
      val f = mathx.map(d, 0, 500, 0.3, .9)
      pic.scale(f)
      draw(pic)
    }

    drawLoop {
      erasePictures()
      repeatFor(rangeTill(0, cheight, tileHeight)) { posY =>
        repeatFor(rangeTill(0, cwidth, tileWidth)) { posX =>
          block(posX, posY)
        }
      }
    }
  }

  def drawHide1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    drawStage(black)

    val carE = trans(2, 14) -> Picture {
      repeat(2) {
        forward(70); right(45); forward(20); right(45)
        forward(18); right(45); forward(20); right(45)
      }
    }

    def car(img: String) = Picture.image(url(img), carE)

    val urlBase = "https://kojofiles.netlify.app"
    val player = car(s"$urlBase/car1.png")
    draw(player)
    drawAndHide(carE)
  }

  def originTopLeft1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    clear()
    originTopLeft()
    repeat(4) {
      forward(100)
      right(90)
    }
  }

  def ticTacToe(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val cb = canvasBounds
    setBackground(black)
    disablePanAndZoom()
    val len = 100

    class Board(bx: Double, by: Double) {
      val margin = 20
      val len2 = len - 2 * margin
      val lineWidth = 8

      def background() {
        setPenColor(null)
        setFillColor(black)
        val mgn = lineWidth / 2
        setPosition(mgn, mgn)
        repeat(4) {
          forward(len - 2 * mgn)
          right(90)
        }
      }

      def cross = Picture {
        background()
        setPenThickness(lineWidth)
        setPenColor(ColorMaker.hsl(200, 1.00, 0.50))
        setPosition(margin, margin)
        lineTo(len - margin, len - margin)
        setPosition(len - margin, margin)
        lineTo(margin, len - margin)
      }

      def o = Picture {
        background()
        setPenThickness(lineWidth)
        setPenColor(ColorMaker.hsl(120, 0.86, 0.64))
        setPosition(len / 2, margin)
        setHeading(0)
        left(360, len2 / 2)
      }

      def blank = Picture {
        background()
      }

      val lines = Picture {
        setPenThickness(lineWidth)
        repeatFor(1 to 2) { n =>
          setPosition(len * n, 0)
          lineTo(len * n, 3 * len)
        }
        repeatFor(1 to 2) { n =>
          setPosition(0, len * n)
          lineTo(3 * len, len * n)
        }
      }

      val pics = Array.ofDim[Picture](3, 3)
      val state = Array.ofDim[Int](3, 3)

      var nextCross = true
      var done = false

      def show() {
        lines.setPosition(bx, by)
        draw(lines)
        repeatFor(0 until 3) { x =>
          repeatFor(0 until 3) { y =>
            val pic = blank
            pic.setPosition(bx + x * len, by + y * len)
            draw(pic)
            pic.onMouseClick { (_, _) =>
              if (!done) {
                val newPic = if (nextCross) {
                  val np = cross
                  np.setPosition(pic.position)
                  state(x)(y) = 2
                  np
                }
                else {
                  val np = o
                  np.setPosition(pic.position)
                  state(x)(y) = 1
                  np
                }
                nextCross = !nextCross
                pics(x)(y) = newPic
                draw(newPic)
                pic.erase()
                checkWin()
                if (!done) {
                  checkDraw()
                }
              }
            }
            pics(x)(y) = pic
            state(x)(y) = 0
          }
        }
      }

      def column(x: Int) = state(x)
      def row(y: Int) = Array(state(0)(y), state(1)(y), state(2)(y))
      def diagonal1 = Array(state(0)(0), state(1)(1), state(2)(2))
      def diagonal2 = Array(state(0)(2), state(1)(1), state(2)(0))

      def checkWinFor(n: Int): Boolean = {
        var win = false
        val target = Array(n, n, n)
        repeatFor(0 until 3) { x =>
          win = column(x).sameElements(target)
          if (win) {
            return true
          }
        }

        repeatFor(0 until 3) { y =>
          win = row(y).sameElements(target)
          if (win) {
            return true
          }
        }
        win = diagonal1.sameElements(target)
        if (win) {
          return true
        }
        win = diagonal2.sameElements(target)
        win
      }

      def gameOver(msg: String) {
        val pmsg = Picture {
          setPenFontSize(80)
          setPenColor(white)
          write(msg)
        }
        val pic = picColCentered(pmsg, Picture.vgap(cb.height - 100))
        drawCentered(pic)
        done = true
      }

      def checkWin() {
        if (checkWinFor(1)) {
          gameOver("O Won")
        }
        else if (checkWinFor(2)) {
          gameOver("X Won")
        }
      }

      def checkDraw() {
        var filled = true
        repeatFor(0 until 3) { x =>
          repeatFor(0 until 3) { y =>
            if (state(x)(y) == 0) {
              filled = false
            }
          }
        }
        if (filled) {
          done = true
          gameOver("It's a Draw")
        }
      }
    }

    val boardSize = len * 3

    setup {
      val b = new Board(cb.x + (cb.width - boardSize) / 2, cb.y + (cb.height - boardSize) / 2)
      b.show()
    }
  }

  def drawCentered1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val pic1 = Picture.rectangle(100, 100)
    val pic4 = Picture.rectangle(100, 20)
    drawCentered(pic1)
    draw(pic4)
  }

  def drawCentered2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val pic1 = Picture.rectangle(100, 50)
    val pic2 = Picture.rectangle(50, 100)
    val pic3 = Picture.rectangle(100, 50)
    val pic = picRow(pic1, pic2, pic3)
    val pic4 = Picture.rectangle(100, 20)
    drawCentered(pic)
    draw(pic4)
  }

  def drawCentered3(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val msg = "Game over"
    val pmsg = Picture {
      setPenFontSize(80)
      setPenColor(blue)
      write(msg)
    }
    val pic = picColCentered(pmsg, Picture.vgap(36))
    val pic4 = Picture.rectangle(100, 20)
    drawCentered(pic)
    draw(pic4)
  }

  def batchPic1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val p1 = Picture.rectangle(50, 50)
    val p2 = rot(45) -> Picture.rectangle(50, 50)
    val pic = picBatch(p1, p2)
    draw(pic)

    animate {
      pic.showNext()
    }
  }

  def canvasBoundsAfterSize(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    size(600, 600)
    cleari()
    drawStage(ColorMaker.black)
    val cb = canvasBounds

    class Ball {
      val pic = Picture.circle(10)
      val pic2 = Picture {
        right(45)
        forward(800)
      }

      val pic23 = Picture {
        left(90)
        forward(80)
      }
      pic.setFillColor(red)
      pic.setPosition(cb.x + 100, cb.y + 400)
      var vel = Vector2D(4, 20)
      val gravity = Vector2D(0, -0.1)

      def draw() {
        pic.draw()
        pic2.draw()
        pic23.draw()
      }

      def step() {
        vel = vel + gravity
        vel = vel.limit(10)
        pic.translate(vel)
        if (pic.collidesWith(stageBorder)) {
          vel = bouncePicVectorOffStage(pic, vel)

        }
        if (pic.collidesWith(pic2)) {
          vel = bouncePicOffPic(pic, vel, pic2)
          pic.translate(vel)

        }
        if (pic.collidesWith(pic23)) {
          vel = bouncePicOffPic(pic, vel, pic23)
          pic.translate(vel)

        }

      }
    }

    val ball = new Ball()
    ball.draw()

    animate {
      ball.step()
    }
  }

  def flappyBall(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    // Use the up/down keys to prevent the ball from colliding with the
    // oncoming obstacles or the stage border.
    // You win if you keep the ball in play for a minute
    cleari()
    drawStage(black)
    // setRefreshRate(50)

    val cb = canvasBounds
    def obstacle(h: Int, w: Int) = Picture.rect(h, w)
    val playerE = trans(49, 31) -> Picture.circle(30)

    var obstacles = Set.empty[Picture]
    def createObstacle() {
      println("obstacle created")
      val height = random((0.5 * cb.height).toInt) + 50
      val trxy = if (randomBoolean) (cb.width / 2, cb.height / 2 - height)
      else (cb.width / 2, -cb.height / 2)
      val obs = fillColor(ColorMaker.blueViolet) * penColor(noColor) *
        trans(trxy._1, trxy._2) -> obstacle(height, random(30) + 30)
      obstacles += obs
      draw(obs)
    }

    val speed = -5
    val pspeed = 5
    val gravity = 0.1
    var fallSpeed = 0.0
    val urlBase = "https://kojofiles.netlify.app"

    val pl1 = Picture.image(url(s"$urlBase/flappy-ball/ballwing1.png"), playerE)
    val pl2 = Picture.image(url(s"$urlBase/flappy-ball/ballwing2.png"), playerE)
    val player = picBatch(pl1, pl2)
    draw(player)
    drawAndHide(playerE)
    //    createObstacle()
    var lastObsCreateTime = epochTime

    animate {
      val currTime = epochTime
      if (currTime - lastObsCreateTime > 1) {
        createObstacle()
        lastObsCreateTime = currTime
      }

      obstacles foreach { obs =>
        if (obs.position.x + 60 < cb.x) {
          obs.erase()
          obstacles -= obs
        }
        else {
          obs.translate(speed, 0)
          if (player.collidesWith(obs)) {
            player.setOpacity(0.3)
            drawMessage("You Lose", Color(255, 24, 27))
            stopAnimation()
          }
        }
      }

      player.showNext()
      if (isKeyPressed(Kc.VK_UP)) {
        fallSpeed = 0
        player.translate(0, pspeed)
      }
      else if (isKeyPressed(Kc.VK_DOWN)) {
        fallSpeed = 0
        player.translate(0, -pspeed)
      }
      else {
        fallSpeed = fallSpeed + gravity
        player.translate(0, -fallSpeed)
      }
      if (player.collidesWith(stageBorder)) {
        player.setOpacity(0.3)
        drawMessage("You Lose", red)
        stopAnimation()
      }
    }

    def drawMessage(m: String, c: Color) {
      drawCenteredMessage(m, c, 30)
    }

    def manageGameTime() {
      showGameTime(60, "You Win", green, 20)
    }

    manageGameTime()
    activateCanvas()
  }

  def fullScreenCanvas(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val startButton = fillColor(red) -> Picture.rectangle(100, 100)
    draw(startButton)
    class Game() {
      drawStage(blue)
      val pic = fillColor(blue) -> Picture.rectangle(50, 50)
      draw(pic)
      var vel = Vector2D(2, 3)
      animate {
        pic.translate(vel)
        if (pic.collidesWith(stageBorder)) {
          vel = bouncePicOffStage(pic, vel)
        }
      }
    }

    startButton.onMousePress { (x, y) =>
      toggleFullScreenCanvas()
      schedule(.1) {
        val game = new Game
      }
      startButton.erase()
    }
  }

  def setPosRotScale(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    showAxes()
    showGrid()
    val pic0 = Picture.circle(10)
    draw(pic0)

    val pic = Picture.rectangle(100, 50)
    pic.setRotation(45)
    pic.setPosition(100, 50)
    pic.setScale(2)
    draw(pic)
  }

  def collidiumGame2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    // Sling the ball (with the mouse) towards the target on the top-right.
    // Then draw paddles on the canvas (with the mouse) to guide the ball
    // away from the obstacles and towards the target.
    // You win if you hit the target within a minute
    switchToDefault2Perspective()
    cleari()
    drawStage(black)
    val cb = canvasBounds
    val obsDelta = cb.width / 4
    val ballDeltaBase = (obsDelta / 4).toInt
    def ballDelta = ballDeltaBase + random(ballDeltaBase)
    val ballSize = 20

    val urlBase = "https://kojofiles.netlify.app"
    val ballE = penColor(red) * trans(ballSize, ballSize) -> Picture.circle(ballSize)
    val ball1 = Picture.image(s"$urlBase/collidium/ball1.png", ballE)
    val ball2 = Picture.image(s"$urlBase/collidium/ball2.png", ballE)
    val ball3 = Picture.image(s"$urlBase/collidium/ball3.png", ballE)
    val ball4 = Picture.image(s"$urlBase/collidium/ball4.png", ballE)

    val ball = picBatch(ball1, ball2, ball3, ball4)
    ball.translate(cb.x + ballDelta, cb.y + ballDelta)

    val target = trans(-cb.x - ballDelta, -cb.y - ballDelta) *
      penColor(cm.lightGreen) * fillColor(cm.lightGreen) -> PicShape.circle(ballSize / 4)

    val obstacles = (1 to 3).map { n =>
      trans(cb.x + n * obsDelta, cb.y + cb.height / 4) * fillColor(cm.lightSteelBlue) * penColor(noColor) -> PicShape.rect(cb.height / 2, 12)
    }

    draw(ball, target)
    drawAndHide(ballE)
    obstacles.foreach { o => draw(o) }
    playMp3Sound("/media/collidium/hit.mp3")

    import collection.mutable.ArrayBuffer
    def line(ps: ArrayBuffer[Point], c: Color) = Picture {
      val sqsz = 4
      def sq() {
        hop(-sqsz / 2)
        repeat(4) {
          forward(sqsz)
          right(90)
        }
        hop(sqsz / 2)
      }
      setPenColor(c)
      setFillColor(c)
      setPosition(ps(0).x, ps(0).y)
      lineTo(ps(1).x, ps(1).y)
      hop(-sqsz / 2)
      left(90)
      sq()
      right(90)
      setPosition(ps(0).x, ps(0).y)
      hop(-sqsz / 2)
      left(90)
      sq()
    }
    val slingPts = ArrayBuffer.empty[Point]
    var sling = Picture.hline(1)
    var paddle = Picture.hline(1)
    var tempPaddle = paddle
    drawAndHide(paddle)

    ball.onMousePress { (x, y) =>
      slingPts += Point(ball.position.x + ballSize, ball.position.y + ballSize)
    }

    ball.onMouseDrag { (x, y) =>
      if (slingPts.size > 1) {
        slingPts.remove(1)
      }
      slingPts += Point(x, y)
      sling.erase()
      sling = line(slingPts, green)
      sling.draw()
    }

    ball.onMouseRelease { (x, y) =>
      sling.erase()
      ball.forwardInputTo(stageArea)
      var vel = if (slingPts.size == 1)
        Vector2D(1, 1)
      else
        Vector2D(slingPts(0).x - slingPts(1).x, slingPts(0).y - slingPts(1).y).limit(7)

      animate {
        ball.translate(vel)
        ball.showNext()
        if (ball.collidesWith(stageBorder)) {
          playMp3Sound("/media/collidium/hit.mp3")
          vel = bouncePicVectorOffStage(ball, vel)
        }
        else if (ball.collidesWith(paddle)) {
          playMp3Sound("/media/collidium/hit.mp3")
          vel = bouncePicVectorOffPic(ball, vel, paddle)
          ball.translate(vel)
        }
        else if (ball.collidesWith(target)) {
          target.setPenColor(green)
          target.setFillColor(green)
          drawCenteredMessage("Yaay! You Win", green, 20)
          stopAnimation()
          playMp3Sound("/media/collidium/win.mp3")
        }

        ball.collision(obstacles) match {
          case Some(obstacle) =>
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffPic(ball, vel, obstacle)
            while (ball.collidesWith(obstacle)) {
              ball.translate(vel)
            }
          case None =>
        }

      }
      showGameTime(60, "Time up! You Lose", cm.lightBlue, 20)
    }

    val paddlePts = ArrayBuffer.empty[Point]
    stageArea.onMousePress { (x, y) =>
      paddle.erase()
      paddlePts.clear()
      paddlePts += Point(x, y)
    }

    stageArea.onMouseDrag { (x, y) =>
      if (paddlePts.size > 1) {
        paddlePts.remove(1)
      }
      paddlePts += Point(x, y)
      tempPaddle.erase()
      tempPaddle = line(paddlePts, ColorMaker.aquamarine)
      tempPaddle.draw()
    }

    stageArea.onMouseRelease { (x, y) =>
      if (tempPaddle.collidesWith(ball)) {
        tempPaddle.erase()
      }
      else {
        paddle = tempPaddle
        paddle.setPenColor(cm.goldenrod)
        paddle.setFillColor(cm.goldenrod)
        paddle.forwardInputTo(stageArea)
      }
    }

    target.forwardInputTo(stageArea)
    obstacles.foreach { o => o.forwardInputTo(stageArea) }
    // Game idea and sounds from https://github.com/shadaj/collidium
  }

  def joystick1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    drawStage(black)
    val cb = canvasBounds
    disablePanAndZoom()

    val rad = 20
    val pic = Picture.rectangle(50, 50)
    pic.setFillColor(red)
    draw(pic)

    val js = joystick(rad)
    js.draw()
    js.setPostiion(cb.x + rad, cb.y + rad)
    animate {
      val vel = js.currentVector
      pic.translate(vel)
      if (pic.collidesWith(stageBorder)) {
        val vel2 = -vel.normalize
        while (pic.collidesWith(stageBorder)) {
          pic.translate(vel2)
        }
      }
    }
  }

  def huntedWithJoystick(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    class Game {
      drawStage(ColorMaker.khaki)
      val cb = canvasBounds

      def gameShape(color: Color) = {
        val pic = Picture.rectangle(20, 20)
        pic.setFillColor(color)
        pic.setPenColor(color)
        pic
      }

      val r1 = gameShape(red)
      val r2 = gameShape(red)
      val r3 = gameShape(red)
      val r4 = gameShape(red)
      r1.setPosition(cb.width / 2 - 50, cb.height / 2 - 50)
      r2.setPosition(-cb.width / 2 + 10, cb.height / 2 - 50)
      r3.setPosition(0, cb.height / 2 - 50)
      r4.setPosition(cb.width / 2 - 50, 0)

      val player = gameShape(blue)
      player.setPosition(cb.x + cb.width / 2, cb.y + cb.height / 8)

      draw(r1, r2, r3, r4, player)

      val playerspeed = 6
      var vel1 = Vector2D(-3, -2)
      var vel2 = Vector2D(3, -2)
      var vel3 = Vector2D(0, -4)
      var vel4 = Vector2D(-4, 0)

      val rs = Seq(r1, r2, r3, r4)
      var rsVels = Map(
        r1 -> vel1,
        r2 -> vel2,
        r3 -> vel3,
        r4 -> vel4
      )

      val rad = 40
      val js = joystick(rad)
      js.draw()
      js.setPostiion(cb.x + cb.width / 2, cb.y + rad)

      animate {
        rs.foreach { r =>
          r.translate(rsVels(r))
        }

        rs.foreach { r =>
          if (r.collidesWith(stageBorder)) {
            val newVel = bouncePicVectorOffStage(r, rsVels(r))
            rsVels += (r -> newVel)
          }

          if (player.collidesWith(r)) {
            gameLost()
          }
        }
        js.movePlayer(player, 0.25)
      }

      def gameLost() {
        drawCenteredMessage("You Lose", red, 30)
        stopAnimation()
        player.setFillColor(purple)
        player.scale(1.1)
      }

      showGameTime(60, "You Win", green)
      activateCanvas()
    }

    val startButton = fillColor(red) -> Picture.rectangle(100, 100)
    val msg = Picture.text("Click to Begin", 30)
    val pic = picColCentered(msg, Picture.vgap(10), startButton)
    drawCentered(pic)
    pic.onMouseClick { (x, y) =>
      pic.erase()
      toggleFullScreenCanvas()
      schedule(1) {
        new Game
      }
    }
  }

  def constrainedJoystick(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    drawStage(ColorMaker.khaki)
    val cb = canvasBounds

    def gameShape(color: Color) = {
      val pic = Picture.rectangle(20, 20)
      pic.setFillColor(color)
      pic.setPenColor(color)
      pic
    }

    val player = gameShape(blue)
    val player2 = gameShape(green)
    player.setPosition(cb.x + cb.width / 2, cb.y + cb.height / 8)
    player2.setPosition(cb.x + cb.width / 2, cb.y + cb.height / 8 + 20)

    draw(player, player2)

    val playerspeed = 6
    val rad = 40
    val js = joystick(rad)
    js.draw()
    js.setPostiion(cb.x + cb.width / 2, cb.y + rad)

    animate {
      js.movePlayer(player, 0.25)
      js.movePlayer(player2, 0.25, Vector2D(0, -1))
    }

    showGameTime(60, "You Win", green)
    activateCanvas()
  }

  def copy1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    showAxes()
    def p = PictureT { t =>
      import t._
      repeat(4) {
        forward(30)
        right(90)
      }
    }
    val pic1 = p
    val pic2 = trans(50, 0) -> p
    val pic3 = trans(100, 0) -> p
    val pictures = Seq(pic1, pic2, pic3)

    // do something for each element in the sequence
    pictures.foreach { p =>
      draw(p)
    }

    // map a sequence to convert it to another sequence
    val pictures2 = pictures.map { p =>
      rot(45) * fillColor(blue) -> p.copy
    }

    // then do something for each element in the new sequence
    pictures2.foreach { p =>
      draw(p)
    }

    // filter a sequence to get a sub-sequence
    val pictures3 = pictures.filter { p =>
      p.position.x > 50
    }

    // then do something for each element in the new sequence
    pictures3.foreach { p =>
      draw(rot(-30) * fillColor(green) -> p.copy)
    }
  }

  def gpicsBoxCollision(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    val psize = 50.0
    val delta = 0.01

    def testBox0(n: Double) = PictureT { t =>
      import t._
      repeat(4) {
        forward(n)
        right
      }
    }

    def testBox = testBox0(psize)

    val p1 = trans(-psize / 2, 0) -> testBox
    val p2 = trans(psize / 2, 0) -> testBox
    val p3 = penColor(blue) * trans(psize / 2 + psize, 0) -> testBox
    val gpics = GPics(p1, p2)
    gpics.draw()
    //    p3.draw()
  }

  def turtlePicCopy(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val pic = Picture {
      repeat(4) {
        forward(100)
        right(90)
      }
    }

    val pic2 = trans(50, 10) -> pic.copy

    draw(pic, pic2)
  }

  def fractalTree2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    val size = 100
    val S = Picture {
      repeat(4) {
        forward(size)
        right()
      }
    }

    def stem = scale(0.13, 1) * penColor(noColor) * fillColor(black) -> S

    clear()
    setBackground(Color(255, 170, 29))
    invisible()

    def drawing(n: Int): Picture = {
      if (n == 1)
        stem
      else
        GPics(
          stem,
          trans(0, size - 5) -> GPics(
            rot(25) * scale(0.72) -> drawing(n - 1),
            rot(-50) * scale(0.55) -> drawing(n - 1)
          )
        )
    }

    val pic = trans(0, -100) -> drawing(10)
    draw(pic)
  }

  def canvasBounds1(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    originTopLeft()
    drawStage(blue)
    val cb = canvasBounds
    println(cb.x, cb.y, cb.width, cb.height)
    repeat(4) {
      forward(100)
      right(90)
    }
  }

  def canvasBounds2(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    zoomXY(0.5, 0.5, 100, 0)
    zoomXY(0.5, 0.5, 100, 0)
    drawStage(blue)
    showAxes()
    val cb = canvasBounds
    println(cb.x, cb.y, cb.width, cb.height)
    setSpeed(superFast)
    repeat(4) {
      forward(100)
      right(90)
    }
  }

  def bugsWithJoystick(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    class Game {
      drawStage(ColorMaker.hsl(198, 1.00, 0.86))
      val cb = canvasBounds
      val platformHeight = 65
      val urlBase = "https://kojofiles.netlify.app"
      def bug = Picture.image(url(s"$urlBase/bug_1.png"))
      val platform = Picture.rectangle(cb.width, platformHeight)
      platform.setPenColor(cm.green)
      platform.setFillColor(cm.green)
      platform.setPosition(cb.x, cb.y)
      val player = Picture.image(url(s"$urlBase/codey.png"))
      var score = 0
      var scoreText = "Your score: 0"

      val bugs = HashSet.empty[Picture]

      player.setPosition(0, cb.y + platformHeight)
      player.scale(0.5)
      draw(player)

      platform.setPosition(cb.x, cb.y)
      draw(platform)

      def bugGen() {
        val bugn = bug
        bugn.setPosition(cb.x + random(cb.width.toInt), cb.y + cb.height)
        draw(bugn)
        bugs += bugn
      }

      timer(200) {
        bugGen()
      }

      var bugVel = Vector2D(0, -5)
      val playerSpeed = 7
      val js = joystick(30)
      js.setPostiion(cb.x + cb.width / 2, cb.y + 30)
      js.draw()
      animate {
        bugs.foreach { b =>
          b.translate(bugVel)
        }

        bugs.foreach { b =>
          if (b.collidesWith(player)) {
            stopAnimation()
            drawCenteredMessage("You Lose", red, 40)
          }
          else if (b.collidesWith(platform)) {
            b.erase()
            bugs -= b
          }
        }
        js.movePlayer(player, 0.25, Vector2D(1, 0))

      }
      showGameTime(30, "You Win", black, 20)
      showFps(black, 16)
      activateCanvas()
      // game images from codeacademy
    }

    val startButton = fillColor(red) -> Picture.rectangle(100, 100)
    val msg = penColor(black) -> Picture.text("Click to Begin", 30)
    val pic = picColCentered(msg, Picture.vgap(10), startButton)
    drawCentered(pic)
    pic.onMouseClick { (x, y) =>
      pic.erase()
      toggleFullScreenCanvas()
      schedule(1.0) {
        new Game()
      }
    }
  }

  def howlerPlay(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    val startButton = fillColor(red) -> Picture.rectangle(100, 100)
    val msg = penColor(black) -> Picture.text("Click to Begin", 30)
    val pic = picColCentered(msg, Picture.vgap(10), startButton)
    drawCentered(pic)
    pic.onMouseClick { (x, y) =>
      pic.erase()
      schedule(1.0) {
        playMp3("https://kojofiles.netlify.app/music-loops/Cave.mp3")
        schedule(2) {
          stopMp3()
        }
      }
    }
  }

  def huntedWithSound(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    class Game {
      drawStage(ColorMaker.khaki)
      val cb = canvasBounds

      def gameShape(color: Color) = {
        val pic = Picture.rectangle(20, 20)
        pic.setFillColor(color)
        pic.setPenColor(color)
        pic
      }

      val r1 = gameShape(red)
      val r2 = gameShape(red)
      val r3 = gameShape(red)
      val r4 = gameShape(red)
      r1.setPosition(cb.width / 2 - 50, cb.height / 2 - 50)
      r2.setPosition(-cb.width / 2 + 10, cb.height / 2 - 50)
      r3.setPosition(0, cb.height / 2 - 50)
      r4.setPosition(cb.width / 2 - 50, 0)

      val player = gameShape(blue)
      player.setPosition(cb.x + cb.width / 2, cb.y + cb.height / 8)

      draw(r1, r2, r3, r4, player)

      val playerspeed = 6
      var vel1 = Vector2D(-3, -2)
      var vel2 = Vector2D(3, -2)
      var vel3 = Vector2D(0, -4)
      var vel4 = Vector2D(-4, 0)

      val rs = Seq(r1, r2, r3, r4)
      var rsVels = Map(
        r1 -> vel1,
        r2 -> vel2,
        r3 -> vel3,
        r4 -> vel4
      )

      val rad = 40
      val js = joystick(rad)
      js.draw()
      js.setPostiion(cb.x + cb.width / 2, cb.y + rad)

      val urlBase = "https://kojofiles.netlify.app/music-loops"
      preloadMp3(s"$urlBase/DrumBeats.mp3")
      playMp3Loop(s"$urlBase/Cave.mp3")

      animate {
        rs.foreach { r =>
          r.translate(rsVels(r))
        }

        rs.foreach { r =>
          if (r.collidesWith(stageBorder)) {
            val newVel = bouncePicVectorOffStage(r, rsVels(r))
            rsVels += (r -> newVel)
          }

          if (player.collidesWith(r)) {
            gameLost()
          }
        }
        js.movePlayer(player, 0.25)
        val dist = player.distanceTo(stageBorder)
        //        println(dist)
        if (dist < 10) {
          //          if (!isMp3Playing) {
          playMp3(s"$urlBase/DrumBeats.mp3")
          //          }
        }
        else {
          //          if (isMp3Playing) {
          stopMp3()
          //          }
        }
      }

      def gameLost() {
        drawCenteredMessage("You Lose", red, 30)
        stopAnimation()
        player.setFillColor(purple)
        player.scale(1.1)
      }

      showGameTime(60, "You Win", green)
      activateCanvas()
    }

    val startButton = fillColor(red) -> Picture.rectangle(100, 100)
    val msg = penColor(black) -> Picture.text("Click to Begin", 30)
    val pic = picColCentered(msg, Picture.vgap(10), startButton)
    drawCentered(pic)
    pic.onMouseClick { (x, y) =>
      pic.erase()
      toggleFullScreenCanvas()
      schedule(1.0) {
        new Game
      }
    }
  }

  def collidiumWithSound(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    // Sling the ball (with mouse/finger) towards the target on the top-right.
    // Then draw paddles on the canvas (with mouse/finger) to guide the ball
    // away from the obstacles and towards the target.
    // You win if you hit the target within a minute
    cleari()
    class Game {
      drawStage(black)
      val cb = canvasBounds
      val obsDelta = cb.width / 4
      val ballDeltaBase = (obsDelta / 4).toInt
      def ballDelta = ballDeltaBase + random(ballDeltaBase)
      val ballSize = 20

      val urlBase = "https://kojofiles.netlify.app"
      val ballE = penColor(red) * trans(ballSize, ballSize) -> Picture.circle(ballSize)
      val ball1 = Picture.image(url(s"$urlBase/collidium/ball1.png"), ballE)
      val ball2 = Picture.image(url(s"$urlBase/collidium/ball2.png"), ballE)
      val ball3 = Picture.image(url(s"$urlBase/collidium/ball3.png"), ballE)
      val ball4 = Picture.image(url(s"$urlBase/collidium/ball4.png"), ballE)

      val ball = picBatch(ball1, ball2, ball3, ball4)
      ball.translate(cb.x + ballDelta, cb.y + ballDelta)

      val target = trans(-cb.x - ballDelta, -cb.y - ballDelta) *
        penColor(cm.lightGreen) * fillColor(cm.lightGreen) -> PicShape.circle(ballSize / 4)

      val obstacles = (1 to 3).map { n =>
        trans(cb.x + n * obsDelta, cb.y + cb.height / 4) * fillColor(cm.lightSteelBlue) * penColor(noColor) -> PicShape.rect(cb.height / 2, 12)
      }

      draw(ball, target)
      drawAndHide(ballE)
      obstacles.foreach { o => draw(o) }
      preloadMp3(s"$urlBase/collidium/hit.mp3")

      import collection.mutable.ArrayBuffer
      def line(ps: ArrayBuffer[Point], c: Color) = penColor(c) -> Picture.fromPath { path =>
        path.moveTo(ps(0).x, ps(0).y)
        path.lineTo(ps(1).x, ps(1).y)
      }
      val slingPts = ArrayBuffer.empty[Point]
      var sling: Picture = Picture.hline(1)
      var paddle: Picture = Picture.hline(1)
      var tempPaddle = paddle
      drawAndHide(paddle)

      ball.onMousePress { (x, y) =>
        slingPts += Point(ball.position.x + ballSize, ball.position.y + ballSize)
      }

      ball.onMouseDrag { (x, y) =>
        if (slingPts.size > 1) {
          slingPts.remove(1)
        }
        slingPts += Point(x, y)
        sling.erase()
        sling = line(slingPts, green)
        sling.draw()
      }

      ball.onMouseRelease { (x, y) =>
        sling.erase()
        ball.forwardInputTo(stageArea)
        var vel = if (slingPts.size == 1)
          Vector2D(1, 1)
        else
          Vector2D(slingPts(0).x - slingPts(1).x, slingPts(0).y - slingPts(1).y).limit(7)

        animate {
          ball.translate(vel)
          ball.showNext()
          if (ball.collidesWith(stageBorder)) {
            playMp3Sound(s"$urlBase/collidium/hit.mp3")
            vel = bouncePicVectorOffStage(ball, vel)
          }
          else if (ball.collidesWith(paddle)) {
            playMp3Sound(s"$urlBase/collidium/hit.mp3")
            vel = bouncePicVectorOffPic(ball, vel, paddle)
            ball.translate(vel)
          }
          else if (ball.collidesWith(target)) {
            target.setPenColor(green)
            target.setFillColor(green)
            drawCenteredMessage("Yaay! You Win", green, 40)
            stopAnimation()
            playMp3Sound(s"$urlBase/collidium/win.mp3")
          }

          ball.collision(obstacles) match {
            case Some(obstacle) =>
              playMp3Sound(s"$urlBase/collidium/hit.mp3")
              vel = bouncePicVectorOffPic(ball, vel, obstacle)
              while (ball.collidesWith(obstacle)) {
                ball.translate(vel)
              }
            case None =>
          }

        }
        showGameTime(60, "Time up! You Lose", cm.lightBlue, 20)
      }

      val paddlePts = ArrayBuffer.empty[Point]
      stageArea.onMousePress { (x, y) =>
        paddle.erase()
        paddlePts.clear()
        paddlePts += Point(x, y)
      }

      stageArea.onMouseDrag { (x, y) =>
        if (paddlePts.size > 1) {
          paddlePts.remove(1)
        }
        paddlePts += Point(x, y)
        tempPaddle.erase()
        tempPaddle = line(paddlePts, ColorMaker.aquamarine)
        tempPaddle.draw()
      }

      stageArea.onMouseRelease { (x, y) =>
        if (tempPaddle.collidesWith(ball)) {
          tempPaddle.erase()
        }
        else {
          paddle = tempPaddle
          paddle.setPenColor(cm.goldenrod)
          paddle.setFillColor(cm.goldenrod)
          paddle.forwardInputTo(stageArea)
        }
      }

      target.forwardInputTo(stageArea)
      obstacles.foreach { o => o.forwardInputTo(stageArea) }
      // Game idea and sounds from https://github.com/shadaj/collidium
    }

    val startButton = fillColor(red) -> Picture.rectangle(100, 100)
    val msg = penColor(black) -> Picture.text("Click to Begin", 30)
    val pic = picColCentered(msg, Picture.vgap(10), startButton)
    drawCentered(pic)
    pic.onMouseClick { (x, y) =>
      pic.erase()
      toggleFullScreenCanvas()
      schedule(1.0) {
        new Game
      }
    }
  }

  def carsWithPreload(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    // Use the four arrow keys to avoid the blue cars
    // You gain energy every second, and lose energy for every collision
    // You lose if your energy drops below zero, or you hit the edges of the screen
    // You win if you stay alive for a minute
    cleari()
    drawStage(black)

    val cb = canvasBounds
    val carHeight = 100
    val markerHeight = 80
    // The collision polygon for the (very similarly sized) car images car1.png and car2.png
    val carE = trans(2, 14) -> Picture {
      repeat(2) {
        forward(70); right(45); forward(20); right(45)
        forward(18); right(45); forward(20); right(45)
      }
    }
    val urlBase = "https://kojofiles.netlify.app"
    def car(img: String) = Picture.image(url(s"$urlBase/$img"), carE)

    val cars = collection.mutable.Map.empty[Picture, Vector2D]
    val carSpeed = 3
    val pResponse = 3
    var pVel = Vector2D(0, 0)
    var disabledTime = 0L

    val bplayer = newMp3Player
    val cplayer = newMp3Player

    preloadImage(s"$urlBase/car-ride/car1.png")
    preloadImage(s"$urlBase/car-ride/car2.png")
    preloadMp3(s"$urlBase/car-ride/car-accel.mp3")
    preloadMp3(s"$urlBase/car-ride/car-brake.mp3")
    preloadMp3(s"$urlBase/car-ride/car-crash.mp3")
    preloadMp3(s"$urlBase/car-ride/car-move.mp3")


    val player = car("car1.png")

    def createCar() {
      val c = trans(player.position.x + randomNormalDouble * cb.width / 10, cb.y + cb.height) ->
        car("car2.png")
      draw(c)
      cars += c -> Vector2D(0, -carSpeed)
    }
    val markers = collection.mutable.Set.empty[Picture]
    def createMarker() {
      val mwidth = 20
      val m = fillColor(white) * penColor(white) *
        trans(cb.x + cb.width / 2 - mwidth / 2, cb.y + cb.height) -> Picture.rect(markerHeight, mwidth)
      draw(m)
      markers += m
    }

    draw(player)
    drawAndHide(carE)

    timer(800) {
      createMarker()
      createCar()
    }

    var energyLevel = 0
    def energyText = s"Energy: $energyLevel"
    val energyLabel = Picture.textu(energyText, 20, ColorMaker.aquamarine)
    energyLabel.translate(cb.x + 10, cb.y + cb.height - 10)
    def updateEnergyTick() {
      energyLevel += 2
      energyLabel.update(energyText)
    }
    def updateEnergyCrash() {
      energyLevel -= 10
      energyLabel.update(energyText)
      if (energyLevel < 0) {
        drawCenteredMessage("You're out of energy! You Lose", red, 30)
        stopAnimation()
      }
    }

    animate {
      player.moveToFront()
      val enabled = epochTimeMillis - disabledTime > 300
      if (enabled) {
        if (isKeyPressed(Kc.VK_LEFT)) {
          pVel = Vector2D(-pResponse, 0)
          player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_RIGHT)) {
          pVel = Vector2D(pResponse, 0)
          player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_UP)) {
          pVel = Vector2D(0, pResponse)
          player.translate(pVel)
          if (!isMp3Playing) {
            playMp3(s"$urlBase/car-ride/car-accel.mp3")
          }
        }
        else {
          stopMp3()
        }
        if (isKeyPressed(Kc.VK_DOWN)) {
          pVel = Vector2D(0, -pResponse)
          player.translate(pVel)
          if (!bplayer.isMp3Playing) {
            bplayer.playMp3(s"$urlBase/car-ride/car-brake.mp3")
          }
        }
        else {
          bplayer.stopMp3()
        }
      }
      else {
        player.translate(pVel)
      }

      if (player.collidesWith(stageLeft) || player.collidesWith(stageRight)) {
        cplayer.playMp3Sound(s"$urlBase/car-ride/car-crash.mp3")
        player.setOpacity(0.5)
        drawCenteredMessage("You Crashed!", red, 30)
        stopAnimation()
      }
      else if (player.collidesWith(stageTop)) {
        pVel = Vector2D(0, -pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
      }
      else if (player.collidesWith(stageBot)) {
        pVel = Vector2D(0, pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
      }

      cars.foreach { cv =>
        val (c, vel) = cv
        c.moveToFront()
        if (player.collidesWith(c)) {
          cplayer.playMp3Sound(s"$urlBase/car-ride/car-crash.mp3")
          pVel = bouncePicVectorOffPic(player, pVel - vel, c) / 2
          player.translate(pVel * 3)
          c.translate(-pVel * 3)
          disabledTime = epochTimeMillis
          updateEnergyCrash()
        }
        else {
          val newVel = Vector2D(vel.x + randomDouble(1) / 2 - 0.25, vel.y)
          cars += c -> newVel
          c.translate(newVel)
        }
        if (c.position.y + carHeight < cb.y) {
          c.erase()
          cars -= c
        }
      }
      markers.foreach { m =>
        m.translate(0, -carSpeed * 2)
        if (m.position.y + markerHeight < cb.y) {
          m.erase()
          markers -= m
        }
      }
    }

    def manageGameScore() {
      var gameTime = 0
      val timeLabel = Picture.textu(gameTime, 20, ColorMaker.azure)
      timeLabel.translate(cb.x + 10, cb.y + 50)
      draw(timeLabel)
      draw(energyLabel)
      timeLabel.forwardInputTo(stageArea)

      timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)
        updateEnergyTick()

        if (gameTime == 60) {
          drawCenteredMessage("Time up! You Win", green, 30)
          stopAnimation()
        }
      }
    }

    manageGameScore()
    playMp3Loop(s"$urlBase/car-ride/car-move.mp3")
    activateCanvas()

    // Car images, via google images, from http://motor-kid.com/race-cars-top-view.html
    // and www.carinfopic.com
    // Car sounds (currenty disabled) from http://soundbible.com
  }

  def selfIntersection(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    val pic1 = Picture.rectangle(100, 100)
    val pic2 = Picture.rectangle(50, 50)
    pic2.setPosition(75, 75)
    draw(pic1, pic2)
    println(pic1.intersection(pic1).toText())
  }

  def evalExpr(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    val ans = eval("var x = 15; x + 3 * 5")
    println(ans)
  }

  def picScreens(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()

    var s1: PicScreen = null
    var s2: PicScreen = null

    class S1 extends PicScreen {
      val p1 = Picture.rectangle(50, 50)
      val p2 = Picture.rectangle(150, 150)
      val p3 = fillColor(green) -> Picture.rectangle(50, 30)
      p3.onMousePress { (x, y) =>
        hide()
        s2.show()
      }
      add(p1, p2, p3)
    }

    class S2 extends PicScreen {
      val p1 = Picture.circle(50)
      val p2 = Picture.circle(150)
      val p3 = fillColor(green) -> Picture.rectangle(50, 30)
      p3.onMousePress { (x, y) =>
        hide()
        s1.show()
      }
      add(p1, p2, p3)
    }

    s1 = new S1()
    s2 = new S2()

    s1.show()
  }

  def huntedWithScreens(): Unit = {
    import kojo.{SwedishTurtle, Turtle, KojoWorldImpl, Vector2D, Picture}
    import kojo.doodle.Color._
    import kojo.Speed._
    import kojo.RepeatCommands._
    import kojo.syntax.Builtins
    implicit val kojoWorld = new KojoWorldImpl()
    val builtins = new Builtins()
    import builtins._
    import turtle._
    import svTurtle._

    cleari()
    drawStage(cm.lavenderBlush)
    val cb = canvasBounds
    var mainmenu: PicScreen = null
    var game: PicScreen = null
    var restart: PicScreen = null
    var settings: PicScreen = null
    var playerlevel = 0
    var score = 0

    class Mainmenu extends PicScreen {
      val start = Picture.text("start", 40)
      start.onMouseClick { (x, y) =>
        erase()
        game = new Game()
        game.show()
      }
      val setting = Picture.text("Settings", 40)
      setting.onMouseClick { (x, y) =>
        erase()
        settings = new Settings()
        settings.show()
      }
      setting.setPosition(0, -60)
      add(start, setting)
    }

    class Game extends PicScreen {
      val pic1 = fillColor(green) * penColor(green) -> Picture.rectangle(30, 30)
      val pic2 = fillColor(black) * penColor(black) -> Picture.rectangle(30, 30)
      val pic3 = fillColor(black) * penColor(black) -> Picture.rectangle(30, 30)

      pic1.setPosition(cb.x + cb.width / 2, cb.y + cb.height / 2)
      pic2.setPosition(cb.x + 50, cb.y + cb.height - 40)
      pic3.setPosition(cb.x + 50, cb.y + cb.height / 2 - 30)

      add(pic1, pic2, pic3)

      var vel1 = Vector2D(2, 6)
      var vel2 = Vector2D(6, 2)
      var vel3 = Vector2D(1, 9)

      onShow {

        timer(1000) {
          score = score + 1
        }

        def gameLost() {
          stopAnimation()
          pic1.setFillColor(black)
          schedule(1) {
            erase()
            restart = new Restart()
            restart.show()
          }
        }

        animate {
          pic2.translate(vel1)
          pic3.translate(vel2)

          if (pic2.collidesWith(stageBorder)) {
            vel1 = bouncePicOffStage(pic2, vel1)
          }
          if (pic3.collidesWith(stageBorder)) {
            vel2 = bouncePicOffStage(pic3, vel2)
          }

          if (pic2.collidesWith(pic3)) {
            vel1 = bouncePicOffPic(pic2, vel1, pic3)
          }

          if (isKeyPressed(Kc.VK_UP)) {
            pic1.translate(0, 5)
          }

          if (isKeyPressed(Kc.VK_DOWN)) {
            pic1.translate(0, -5)
          }

          if (isKeyPressed(Kc.VK_LEFT)) {
            pic1.translate(-5, 0)
          }

          if (isKeyPressed(Kc.VK_RIGHT)) {
            pic1.translate(5, 0)
          }

          if (pic1.collidesWith(pic2)) {
            gameLost()
          }
          if (pic1.collidesWith(pic3)) {
            gameLost()
          }
          if (pic1.collidesWith(stageBorder)) {
            gameLost()
          }
        }
      }

    }
    class Restart extends PicScreen {
      def msg = s"$playerlevel"
      val level = Picture.textu(msg, 40, red)
      def levelup() {
        level.update(msg)
      }
      if (score >= 10) {
        score = 0
        playerlevel = playerlevel + 1
        levelup()
      }
      val restart = Picture.text("restart", 40)
      restart.onMouseClick { (x, y) =>
        erase()
        game = new Game()
        game.show()
      }
      val menu = Picture.text("Main Menu", 40)
      menu.onMouseClick { (x, y) =>
        erase()
        mainmenu = new Mainmenu()
        mainmenu.show()
      }
      menu.setPosition(0, -60)
      level.setPosition(0, 60)
      add(restart, menu, level)
    }
    class Settings extends PicScreen {
      val gray = fillColor(cm.gray) * penColor(cm.gray) ->
        Picture.rectangle(30, 30)
      gray.onMouseClick { (x, y) =>
        drawStage(cm.gray)
        erase()
        mainmenu = new Mainmenu()
        mainmenu.show()
      }
      val blue = fillColor(cm.blue) * penColor(cm.blue) ->
        Picture.rectangle(30, 30)
      blue.onMouseClick { (x, y) =>
        drawStage(cm.blue)
        erase()
        mainmenu = new Mainmenu()
        mainmenu.show()
      }
      val bgc = picRowCentered(gray, Picture.hgap(10), blue)
      add(bgc)
    }
    settings = new Settings
    game = new Game()
    restart = new Restart
    mainmenu = new Mainmenu
    mainmenu.show()
    disablePanAndZoom()
  }
}
