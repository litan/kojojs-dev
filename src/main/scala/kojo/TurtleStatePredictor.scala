package kojo

/** Used to predict state at end of the world for Turtle */
trait TurtleStatePredictor {
  var _x: Double = 0.0
  var _y: Double = 0.0
  var _headingRadians: Double = 0.0
  var _penIsUp = false

  object track { // implement all relevant state modifiers as in Turtle.scala
    def reset(x: Double = 0.0, 
              y: Double = 0.0, 
              headingRadians: Double = 0.0, 
              penIsUp: Boolean = false): Unit = {
      _x = x
      _y = y 
      _headingRadians = headingRadians
      _penIsUp = penIsUp

    }

    def forward(n: Double): Unit = {
      val (pfx, pfy) = TurtleHelper.posAfterForward(_x, _y, _headingRadians, n)
      _x = pfx
      _y = pfy
    }

    def penDown(): Unit = _penIsUp = false
    def penUp(): Unit = _penIsUp = true

    def turn(angle: Double): Unit = _headingRadians += Utils.deg2radians(angle)
    def setPosition(x: Double, y: Double): Unit = {
      _x = x
      _y = y
    }
    def setHeadingRadians(radians: Double) = _headingRadians = radians
  }
  
  object predict {
    def position: shape.Point = shape.Point(_x, _y)
    def heading: Double = Utils.rad2degrees(_headingRadians)

    object style {
      def down: Boolean = !_penIsUp
    }
  }
}