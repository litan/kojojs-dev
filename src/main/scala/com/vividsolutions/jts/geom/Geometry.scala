package com.vividsolutions.jts.geom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("jsts.geom.Geometry")
class Geometry extends js.Object {
  def intersects(other: Geometry): Boolean = js.native
  def union(other: Geometry): Geometry = js.native
}

