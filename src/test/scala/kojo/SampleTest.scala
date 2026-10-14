package kojo

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SampleTest extends AnyFunSuite with Matchers {

  test("Something") {
    println("Hello Test")
    implicit val kojoWorld = new TestKojoWorld()
    println("Hello Test2")
    (1 + 1) should be (2)
  }

}
