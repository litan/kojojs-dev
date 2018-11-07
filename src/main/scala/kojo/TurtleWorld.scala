package kojo

import scala.scalajs.js

import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.window

import kojo.doodle.Color
import pixiscalajs.PIXI
import pixiscalajs.PIXI.RendererOptions

class TurtleWorld {
  PIXI.Pixi
  val fiddleContainer =
    document.getElementById("fiddle-container").asInstanceOf[html.Div]
  val canvas_holder =
    document.getElementById("canvas-holder").asInstanceOf[html.Div]
  val (width, height) =
    (fiddleContainer.clientWidth, fiddleContainer.clientHeight)
  var renderer = PIXI.Pixi.autoDetectRenderer(width, height, rendererOptions())
  val stage = new PIXI.Container()
  init()

  def init() {
    render()
    canvas_holder.appendChild(renderer.view)
    stage.name = "Stage"
    stage.width = width
    stage.height = height
    stage.interactive = true
    stage.setTransform(width / 2, height / 2, 1, -1, 0, 0, 0, 0, 0)
    initEvents()
  }

  def addTurtleLayer(layer: PIXI.Container): Unit = {
    stage.addChild(layer)
  }

  val MaxBurst = 100
  var burstCount = 0
  def scheduleLater(fn: () => Unit): Unit = {
    burstCount += 1
    if (burstCount < MaxBurst) {
      fn()
    }
    else {
      window.setTimeout(fn, 0)
      burstCount = 0
    }
  }

  def addSprite(sprite: PIXI.Sprite): Unit = {
    def endFrame(): Unit = {
      stage.addChild(sprite)
      render()
    }

    scheduleLater(endFrame)
  }

  def render(): Unit = {
    renderer.render(stage)
  }

  def rendererOptions(
    antialias:         Boolean = true,
    resolution:        Double  = 1,
    backgroundColor:   Int     = 0xFFFFFF,
    clearBeforeRender: Boolean = true
  ): RendererOptions = {
    js.Dynamic
      .literal(
        antialias = antialias,
        resolution = resolution,
        backgroundColor = backgroundColor,
        clearBeforeRender = clearBeforeRender
      )
      .asInstanceOf[RendererOptions]
  }

  def setBackground(color: Color): Unit = {
    renderer.backgroundColor = color.toRGBDouble
  }

  var animatiing = false
  def animate(fn: => Unit): Unit = {
    animatiing = true
    window.requestAnimationFrame { t =>
      fn
      if (animatiing) {
        animate(fn)
      }
    }
  }

  def stopAnimation(): Unit = {
    animatiing = false
  }

  val noPic = TurtlePicture { t =>
  }(this)
  @volatile var stageBorder: Picture = _
  @volatile var stageLeft: Picture = _
  @volatile var stageTop: Picture = _
  @volatile var stageRight: Picture = _
  @volatile var stageBot: Picture = _
  @volatile var stageArea: Picture = _

  def clearStage() {
    stageBorder = noPic
    stageLeft = noPic
    stageTop = noPic
    stageRight = noPic
    stageBot = noPic
  }

  def drawStage(fillc: Color)(implicit turtleWorld: TurtleWorld) {
    def border(size: Double) = TurtlePicture { t =>
      t.setPenThickness(10)
      t.forward(size)
    }

    val xmax = stage.position.x.abs
    val ymax = stage.position.y.abs

    stageLeft = border(height)
    stageLeft.translate(-xmax, -ymax)

    stageTop = border(width)
    stageTop.translate(-xmax, ymax)
    stageTop.rotate(-90)

    stageRight = border(height)
    stageRight.translate(xmax, -ymax)

    stageBot = border(width)
    stageBot.translate(-xmax, -ymax)
    stageBot.rotate(-90)

    stageArea = TurtlePicture { t =>
      t.setFillColor(fillc)
      t.setPenColor(Color.darkGray)
      for (_ <- 1 to 2) {
        t.forward(height)
        t.right()
        t.forward(width)
        t.right()
      }
    }
    stageArea.translate(-xmax, -ymax)

    stageBorder = GPics(
      stageLeft,
      stageTop,
      stageRight,
      stageBot
    )

    stageArea.draw()
    stageBorder.draw()
  }

  def bounceVecOffStage(v: Vector2D, p: Picture): Vector2D = {
    val stageparts = List(stageTop, stageBot, stageLeft, stageRight)
    p.collision(stageparts).map {
      _ match {
        case p if p == stageTop   => Vector2D(v.x, -v.y)
        case p if p == stageBot   => Vector2D(v.x, -v.y)
        case p if p == stageLeft  => Vector2D(-v.x, v.y)
        case p if p == stageRight => Vector2D(-v.x, v.y)
        case _                    => v
      }
    }.get
  }

  def collidesWithStage(p: Picture): Boolean = {
    val stageparts = List(stageTop, stageBot, stageLeft, stageRight)
    p.collision(stageparts).isDefined
  }

  val pressedKeys = new collection.mutable.HashSet[Int]

  def initEvents(): Unit = {
    def keyDown(e: KeyboardEvent): Unit = {
      pressedKeys.add(e.keyCode)
    }
    def keyUp(e: KeyboardEvent): Unit = {
      pressedKeys.remove(e.keyCode)
    }
    window.addEventListener("keydown", keyDown, false)
    window.addEventListener("keyup", keyUp, false)
  }

  def isKeyPressed(keyCode: Int) = pressedKeys.contains(keyCode)
}
