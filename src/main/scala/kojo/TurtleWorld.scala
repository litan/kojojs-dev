package kojo

import scala.scalajs.js

import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.window

import kojo.doodle.Color
import pixiscalajs.PIXI
import pixiscalajs.PIXI.RendererOptions

class TurtleWorld {
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

  def animate(fn: => Unit): Unit = {
    window.requestAnimationFrame { t =>
      fn
      animate(fn)
    }
  }
}
