package kojo

import org.scalajs.dom
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** satıryaz and yaz write to the output panel of the page (the element with id="output"). */
class TurkishOutputTest extends AnyFunSuite with Matchers {
  implicit val kojoWorld: TestKojoWorld = new TestKojoWorld()
  val builtins = new kojo.syntax.Builtins()
  import builtins.trTurtle._

  private def withPanel(body: dom.Element => Unit): Unit = {
    val panel = dom.document.createElement("div")
    panel.setAttribute("id", "output")
    dom.document.body.appendChild(panel)
    try body(panel)
    finally dom.document.body.removeChild(panel)
  }
  private def lines(panel: dom.Element): Seq[String] =
    (0 until panel.childNodes.length).map(i => panel.childNodes(i).textContent)

  test("satıryaz writes one line per call, yaz adds to the open line") {
    withPanel { panel =>
      satıryaz("merhaba")
      yaz("a"); yaz("b"); satıryaz("c")
      satıryaz(42)
      lines(panel) shouldBe Seq("merhaba", "abc", "42")
    }
  }

  test("satıryaz() gives a blank line that has a height") {
    withPanel { panel =>
      satıryaz("bir"); satıryaz(); satıryaz("iki")
      lines(panel) shouldBe Seq("bir", "\u00a0", "iki")
    }
  }

  test("an Aralık is written in the form for learners, not as 'Range 1 until 5'") {
    withPanel { panel =>
      satıryaz(Aralık(1, 5))
      lines(panel) shouldBe Seq("Aralık(1, 2, 3, 4)")
    }
  }

  test("if somebody else wrote into the panel in between, the open line is not reused") {
    withPanel { panel =>
      yaz("açık")
      val other = dom.document.createElement("div")
      other.appendChild(dom.document.createTextNode("başkası"))
      panel.appendChild(other)
      satıryaz("yeni")
      lines(panel) shouldBe Seq("açık", "başkası", "yeni")
    }
  }

  test("without a panel it does not fail (it goes to the console)") {
    satıryaz("panel yok")
    yaz("panel yok")
    succeed
  }
}
