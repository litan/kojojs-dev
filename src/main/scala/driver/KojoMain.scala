package driver

object KojoMain {

  def main(args: Array[String]): Unit = {
    carGame()
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
    val obj = fillColor(red) * penColor(red) -> Picture.circle(10)
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
    val text = Picture.textu(msg, 25, Color.blue)
    draw(text)
    var x = 1
    animate {
      text.update(msg + x)
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
      val layout = layout0.lines.filter { _.length > 0 }.toVector.reverse
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
      val layout = layout0.lines.filter { _.length > 0 }.toVector.reverse
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

    val urlBase = "https://kojofiles.netlify.com"
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
    val pic2 = fillColor(blue) * trans(-200, 100) -> Picture.rectangle(100, 50)
    pic1.onMousePress { (x, y) =>
      println(s"P1 Press: $x, $y")
    }
    pic1.onMouseRelease { (x, y) =>
      println(s"P1 Release: $x, $y")
    }
    pic1.onMouseDrag { (x, y) =>
      println(s"P1 Drag: $x, $y")
    }
    //    pic1.onMouseClick { (x, y) =>
    //      println(s"Click: $x, $y")
    //    }
    //    pic1.onMouseMove { (x, y) =>
    //      println(s"Move: $x, $y")
    //    }
    //    pic1.onMouseEnter { (x, y) =>
    //      println(s"Enter: $x, $y")
    //    }
    //    pic1.onMouseExit { (x, y) =>
    //      println(s"Exit: $x, $y")
    //    }

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

    val ball = PicShape.circle(10)
    ball.setFillColor(red)
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
    ball.onMousePress { (x, y) =>
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
          println("filled")
          paddle.forwardInputTo(stageArea)
        }
      }
    }

    stageArea.onMouseRelease { (x, y) =>
      if (tempPaddle.collidesWith(ball)) {
        tempPaddle.erase()
      }
      else {
        paddle = tempPaddle
        paddle.setPenColor(yellow)
        //        paddle.setFillColor(yellow)
        println("filled")
        paddle.forwardInputTo(stageArea)
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
}
