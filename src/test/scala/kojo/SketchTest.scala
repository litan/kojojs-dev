package kojo

import org.scalajs.dom
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.scalajs.js

/**
 * Sketches (Processing style canvas drawing): `Picture.fromSketch`, `canvasSketch` and `CanvasDraw`.
 *
 * THE EXPECTED VALUES COME FROM REAL JAVA2D: I drew with `Arc2D` / `Rectangle2D` / `Line2D` + `BasicStroke`,
 * which produce exactly what the desktop `CanvasDraw` draws, on a headless (java.awt.headless)
 * `BufferedImage` and read the pixels; the "Java2D:" notes below are those measurements (the direction
 * of the arc, the 3rd and 4th values of rect being the width and the height, the cap styles). A sketch
 * coordinate is a raw image row; see CanvasDraw.
 */
class SketchTest extends AnyFunSuite with Matchers {
  private implicit val w: TestKojoWorld = new TestKojoWorld()
  private val b = new kojo.syntax.Builtins()

  private def context(p: SketchPic) = p.canvasElement.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  /** (r, g, b, alpha) */
  private def pixel(p: SketchPic, x: Int, y: Int): (Int, Int, Int, Int) = {
    val d = context(p).getImageData(x, y, 1, 1).data
    (d(0).toInt, d(1).toInt, d(2).toInt, d(3).toInt)
  }
  private def alpha(p: SketchPic, x: Int, y: Int): Int = pixel(p, x, y)._4

  /** Makes a sketch and runs the setup plus `frames` frames of the loop. */
  private def sketch(setup: CanvasDraw => Unit, loop: CanvasDraw => Unit = _ => (), frames: Int = 1, f: Double = 1): SketchPic = {
    val p = new SketchPic(setup, loop, f, b.setRandomSeed)
    p.draw()
    (1 to frames).foreach(_ => p.step())
    p
  }

  test("setup runs once when the picture is first drawn, drawLoop on every frame; noLoop stops only the sketch") {
    var setupCount, loopCount = 0
    val p = new SketchPic(_ => setupCount += 1, cd => { loopCount += 1; if (loopCount == 3) cd.noLoop() }, 1, b.setRandomSeed)
    setupCount shouldBe 0 // not set up before it is drawn
    p.draw()
    setupCount shouldBe 1
    loopCount shouldBe 0 // the first call of the loop comes from animate
    (1 to 6).foreach(_ => p.step())
    setupCount shouldBe 1
    loopCount shouldBe 3 // noLoop at the 3rd frame: the later frames are skipped
    p.cd.loop shouldBe false
  }

  test("the surface is not cleared between frames: a trail stays (without background the drawings pile up)") {
    var n = 0
    val p = sketch(_ => (), cd => {
      cd.fill(255, 0, 0); cd.noStroke()
      cd.rect(10 + 30 * n, 10, 10, 10)
      n += 1
    }, frames = 3)
    alpha(p, 15, 15) shouldBe 255
    alpha(p, 45, 15) shouldBe 255
    alpha(p, 75, 15) shouldBe 255
    alpha(p, 25, 15) shouldBe 0
  }

  test("rect(x1, y1, x2, y2): the 3rd and 4th values are the width and the height. Java2D: (35,55) is filled, (45,25) (15,65) (5,25) are empty") {
    val p = sketch(cd => { cd.fill(255, 0, 0); cd.noStroke(); cd.rect(10, 20, 30, 40) })
    alpha(p, 35, 55) shouldBe 255
    alpha(p, 45, 25) shouldBe 0
    alpha(p, 15, 65) shouldBe 0
    alpha(p, 5, 25) shouldBe 0
  }

  test("ellipse(cx, cy, w, h): w and h are the FULL size") {
    val p = sketch(cd => { cd.fill(0, 0, 255); cd.noStroke(); cd.ellipse(100, 100, 40, 20) })
    alpha(p, 118, 100) shouldBe 255 // radius 20
    alpha(p, 122, 100) shouldBe 0
    alpha(p, 100, 108) shouldBe 255 // radius 10
    alpha(p, 100, 112) shouldBe 0
  }

  test("arc: angles are radians, a growing angle goes COUNTER-clockwise on raw coordinates. Java2D: upper right (134,66) is filled, lower right (134,134) is empty") {
    val p = sketch(cd => { cd.fill(255, 0, 0); cd.noStroke(); cd.arc(100, 100, 100, 100, 0, math.Pi / 2) })
    alpha(p, 134, 66) shouldBe 255
    alpha(p, 134, 134) shouldBe 0
    alpha(p, 66, 66) shouldBe 0
  }

  test("the arc pen is OPEN: the arc is drawn, the chord (the straight line between the ends) is not. Java2D: the arc (135,65) is filled, the middle of the chord (117,83) is empty") {
    val p = sketch(cd => { cd.noFill(); cd.stroke(255, 0, 0); cd.strokeWeight(2); cd.arc(100, 100, 100, 100, 0, math.Pi / 2) })
    alpha(p, 135, 65) should be > 200 // an edge pixel: the antialiasing differs a little from Java2D (243 / 255)
    alpha(p, 117, 83) shouldBe 0
  }

  test("point: a filled dot with a round cap; the pen cap constants are the same as on the desktop: ROUND, PROJECT (square cap), SQUARE (butt cap!)") {
    val p = sketch(cd => { cd.stroke(255, 0, 0); cd.strokeWeight(20); cd.point(100, 100) })
    alpha(p, 100, 100) shouldBe 255
    alpha(p, 108, 100) shouldBe 255 // Java2D: a round dot of radius 10
    alpha(p, 115, 100) shouldBe 0

    // Java2D: CAP_SQUARE extends the line by half the thickness ((105,100) is filled); CAP_BUTT does not
    val projecting = sketch(cd => { cd.stroke(255, 0, 0); cd.strokeWeight(20); cd.strokeCap(cd.PROJECT); cd.line(50, 100, 100, 100) })
    alpha(projecting, 105, 100) shouldBe 255
    alpha(projecting, 45, 100) shouldBe 255
    alpha(projecting, 115, 100) shouldBe 0
    val butt = sketch(cd => { cd.stroke(255, 0, 0); cd.strokeWeight(20); cd.strokeCap(cd.SQUARE); cd.line(50, 100, 100, 100) })
    alpha(butt, 105, 100) shouldBe 0
    alpha(butt, 45, 100) shouldBe 0
    alpha(butt, 75, 100) shouldBe 255
  }

  test("transforms: translate moves the drawing; pushMatrix/popMatrix save only the transform, not the paint") {
    val p = sketch(cd => {
      cd.noStroke()
      cd.pushMatrix()
      cd.translate(100, 50)
      cd.fill(255, 0, 0)
      cd.rect(0, 0, 10, 10)
      cd.popMatrix()
      cd.rect(0, 0, 10, 10) // the transform is back but the paint STAYED red
    })
    alpha(p, 105, 55) shouldBe 255
    pixel(p, 5, 5) shouldBe ((255, 0, 0, 255))
    alpha(p, 105, 5) shouldBe 0
  }

  test("background paints the whole surface whatever the transform; a transparent background fades the previous one") {
    val p = sketch(cd => {
      cd.translate(300, 300); cd.rotate(0) // fully translated
      cd.background(0, 255, 0)
    })
    pixel(p, 0, 0) shouldBe ((0, 255, 0, 255))
    pixel(p, p.canvasElement.width - 1, p.canvasElement.height - 1) shouldBe ((0, 255, 0, 255))

    val s = sketch(cd => cd.background(255, 255, 255), cd => cd.background(0, 0, 0, 51), frames = 2)
    val (r, _, _, a) = pixel(s, 5, 5)
    a shouldBe 255
    r should be < 255 // it faded over two frames
    r should be > 100 // but did not go fully dark: 255 -> ~204 -> ~163
  }

  test("text is in the pen color and looks UPRIGHT (y is flipped as on the desktop): the bar of the T is ABOVE the baseline (large y)") {
    val p = sketch(cd => { cd.stroke(255, 0, 0); cd.textFont(Font("Sans Serif", 40)); cd.text("T", 50, 100) })
    def total(y0: Int, y1: Int): Int = (for (y <- y0 until y1; x <- 40 until 90) yield alpha(p, x, y)).sum
    total(100, 145) should be > 0 // the glyph goes above the baseline (growing y)
    total(55, 98) shouldBe 0
    // the bar is on top: the upper third of the glyph (large y) is fuller than the lower third (only the thin stem)
    total(120, 132) should be > total(100, 112)
  }

  test("shapes made of vertices: a triangle is filled; arcVertex draws a half circle between two points") {
    val triangle = sketch(cd => {
      cd.fill(255, 0, 0); cd.noStroke()
      cd.beginShape(); cd.vertex(10, 10); cd.vertex(110, 10); cd.vertex(10, 110); cd.endShape()
    })
    alpha(triangle, 30, 30) shouldBe 255
    alpha(triangle, 100, 100) shouldBe 0

    // (100,100) -> (200,100) half circle: center (150,100), radius 50
    val halfCircle = sketch(cd => {
      cd.fill(0, 0, 255); cd.noStroke()
      cd.beginShape(); cd.vertex(100, 100); cd.arcVertex(200, 100, 180); cd.endShape()
    })
    // Java2D (the Rich2DPath formula, Arc2D): at 180 degrees the half disk is on TOP (small y): (150,70) is filled, (150,130) is empty
    alpha(halfCircle, 150, 70) shouldBe 255
    alpha(halfCircle, 150, 130) shouldBe 0
    alpha(halfCircle, 150, 40) shouldBe 0 // outside the radius of 50

    // 90 degrees (100,100) -> (150,50): center (150,100), radius 50. Java2D: (110,70) and (130,60) are filled; (140,80), (115,115) are empty
    val quarter = sketch(cd => {
      cd.fill(0, 0, 255); cd.noStroke()
      cd.beginShape(); cd.vertex(100, 100); cd.arcVertex(150, 50, 90); cd.endShape()
    })
    alpha(quarter, 110, 70) should be >= 245 // an edge pixel (Java2D 254)
    alpha(quarter, 130, 60) shouldBe 255
    alpha(quarter, 140, 80) shouldBe 0
    alpha(quarter, 115, 115) shouldBe 0
  }

  test("scaleOutFactor 2: the surface has twice the pixels and the sprite half the scale; a user coordinate is a surface pixel") {
    val p = sketch(cd => { cd.fill(255, 0, 0); cd.noStroke(); cd.rect(1500, 1100, 10, 10) }, f = 2)
    p.canvasElement.width shouldBe 1600 // TestKojoWorld 800 x 600
    p.canvasElement.height shouldBe 1200
    alpha(p, 1505, 1105) shouldBe 255
    val sprite = p.tnode.asInstanceOf[pixiscalajs.PIXI.Container].children(0)
    sprite.scale.x shouldBe 0.5 +- 1e-12
  }

  test("scaleOutFactor 0 (automatic): min(dpr, 2), small enough for the texture limit, never below 1") {
    val auto = SketchPic.autoFactor _
    auto(1, 800, 600, 8192) shouldBe 1.0
    auto(2.625, 800, 600, 8192) shouldBe 2.0 // the DPR measured on two phones; the upper limit is 2
    auto(1.5, 800, 600, 8192) shouldBe 1.5
    auto(3, 800, 600, 8192) shouldBe 2.0
    auto(2.625, 1400, 600, 2800) shouldBe 2.0 // fits exactly
    auto(2, 1400, 600, 2000) shouldBe (2000.0 / 1400) +- 1e-12 // the texture limit shortens the factor
    auto(2, 5000, 600, 4096) shouldBe 1.0 // even if it does not fit, never below 1
    auto(0.75, 800, 600, 8192) shouldBe 1.0 // a DPR < 1 does not make the factor smaller
    auto(2, 800, 600, 0) shouldBe 2.0 // if the limit is not known it is not applied
  }

  test("scaleOutFactor 0 on the real path: computed from the DPR and the texture limit of the device; the surface and the sprite follow it; a negative value stays 1") {
    val p = new SketchPic(_ => (), _ => (), 0, b.setRandomSeed)
    p.draw()
    val expected = SketchPic.autoFactor(dom.window.devicePixelRatio, 800, 600, SketchPic.maxTextureSize)
    p.factor shouldBe expected
    p.factor should (be >= 1.0 and be <= 2.0)
    p.canvasElement.width shouldBe math.round(800 * expected).toInt
    p.canvasElement.height shouldBe math.round(600 * expected).toInt
    p.tnode.asInstanceOf[pixiscalajs.PIXI.Container].children(0).scale.x shouldBe (1 / expected) +- 1e-12
    SketchPic.maxTextureSize should be >= 2048.0
    // in the automatic mode the coordinates do NOT change with the device: they are stage units
    p.cd.width shouldBe 800.0 +- 1e-9
    p.cd.height shouldBe 600.0 +- 1e-9
    new SketchPic(_ => (), _ => (), -3, b.setRandomSeed).factor shouldBe 1.0
  }

  test("automatic mode (CanvasDraw scale 2): coordinates are stage units; the drawing, the line thickness and the background are twice as big on the surface") {
    val c = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
    c.width = 1600; c.height = 1200
    val cd = new CanvasDraw(c, 1600, 1200, b.setRandomSeed, 2.0)
    cd.width shouldBe 800.0
    cd.height shouldBe 600.0
    cd.background(0, 0, 255) // the whole surface, as (0,0)-(800,600) in stage units
    cd.fill(255, 0, 0); cd.noStroke()
    cd.rect(700, 500, 10, 10) // stage units: (1400,1000)-(1420,1020) on the surface
    val g = c.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    def px(x: Int, y: Int) = { val d = g.getImageData(x, y, 1, 1).data; (d(0).toInt, d(1).toInt, d(2).toInt, d(3).toInt) }
    px(1410, 1010) shouldBe ((255, 0, 0, 255)) // the red square, twice as big
    px(1395, 1010) shouldBe ((0, 0, 255, 255)) // outside the square: the background
    px(1599, 1199) shouldBe ((0, 0, 255, 255)) // the background reaches the last pixel of the surface
    px(0, 0) shouldBe ((0, 0, 255, 255))
    cd.stroke(0, 255, 0); cd.strokeWeight(10); cd.line(100, 100, 300, 100) // a thickness of 20 px on the surface
    px(400, 200) shouldBe ((0, 255, 0, 255))
    px(400, 208) shouldBe ((0, 255, 0, 255)) // twice the thickness (20 px): 8 px above the center it is still green (it would not be with an unscaled thickness of 10 px)
    px(400, 214) shouldBe ((0, 0, 255, 255)) // farther than 10 px: the background
  }

  test("blendMode(\"lighter\") adds the colors") {
    val p = sketch(cd => {
      cd.noStroke(); cd.blendMode("lighter")
      cd.fill(100, 0, 0); cd.rect(0, 0, 10, 10)
      cd.fill(100, 0, 0); cd.rect(0, 0, 10, 10)
    })
    pixel(p, 5, 5)._1 shouldBe 200 +- 2
  }

  test("randomSeed seeds the random generator of Builtins: the same seed gives the same sequence") {
    val p = sketch(cd => cd.randomSeed(7L))
    val a = b.random(1000)
    p.cd.randomSeed(7L)
    a should not be (-1)
    b.random(1000) shouldBe a
  }

  test("erase and then draw again: it works; the GL resource is let go but the surface stays") {
    val p = sketch(cd => { cd.fill(255, 0, 0); cd.rect(0, 0, 20, 20) })
    p.erase()
    p.draw()
    alpha(p, 10, 10) shouldBe 255
  }

  test("no leak: 200 sketches made, drawn and erased, the PIXI texture caches do not grow (Texture.from is not used)") {
    val P = js.Dynamic.global.PIXI.utils
    def say(o: js.Dynamic) = js.Object.keys(o.asInstanceOf[js.Object]).length
    val textureBefore = say(P.TextureCache)
    val baseBefore = say(P.BaseTextureCache)
    (1 to 200).foreach { _ =>
      val p = new SketchPic(_ => (), cd => cd.rect(0, 0, 5, 5), 1, b.setRandomSeed)
      p.draw(); p.step(); p.erase()
    }
    say(P.TextureCache) shouldBe textureBefore
    say(P.BaseTextureCache) shouldBe baseBefore
  }

  test("bounds: from the lower left corner of the canvas, (0,0)-(width, height)") {
    val p = new SketchPic(_ => (), _ => (), 1, b.setRandomSeed)
    val cs = p.picGeom.getCoordinates
    cs.map(_.x).min shouldBe 0.0
    cs.map(_.y).min shouldBe 0.0
    cs.map(_.x).max shouldBe 800.0
    cs.map(_.y).max shouldBe 600.0
  }

  // --- the entry points ---------------------------------------------------------

  test("Picture.fromSketch with a structural type: the setup/drawLoop scripts of the desktop compile and run unchanged") {
    import scala.language.reflectiveCalls
    var setups, loops = 0
    val sketch = new {
      def setup(cd: b.CanvasDraw): Unit = { setups += 1; cd.fill(255, 0, 0); cd.noStroke() }
      def drawLoop(cd: b.CanvasDraw): Unit = { loops += 1; cd.rect(0, 0, 10, 10) }
    }
    val pic = b.Picture.fromSketch(sketch).asInstanceOf[SketchPic]
    pic.draw()
    pic.step(); pic.step()
    setups shouldBe 1
    loops shouldBe 2
    alpha(pic, 5, 5) shouldBe 255
  }

  test("canvasSketch / animateWithCanvasDraw / animateWithSetupCanvasDraw draw (animate does not run in the fake world, it must not blow up)") {
    import scala.language.reflectiveCalls
    var setups = 0
    val sketch = new {
      def setup(cd: b.CanvasDraw): Unit = setups += 1
      def drawLoop(cd: b.CanvasDraw): Unit = ()
    }
    b.canvasSketch(sketch, 1)
    b.animateWithCanvasDraw(cd => cd.noLoop())
    b.animateWithSetupCanvasDraw(cd => setups += 1)(cd => ())
    setups shouldBe 2
  }
}
