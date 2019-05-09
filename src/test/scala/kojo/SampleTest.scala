package kojo

import org.scalatest.FunSuite
import org.scalatest.Matchers

class SampleTest extends FunSuite with Matchers {

  test("Something") {
    println("Hello Test")
    implicit val kojoWorld = new TestKojoWorld()
    println("Hello Test2")
    (1 + 1) should be (2)
  }

}
