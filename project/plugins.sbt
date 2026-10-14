// Scala.js 1.x; 1.20.2 publishes its compiler plugin for both Scala 2.12 and 2.13.
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.20.2")

// jsDependencies (the ProvidedJS pixi/jsts files used by the tests) moved to a separate plugin in Scala.js 1.x
addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")

// Selenium JS environment for the browser tests
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "1.1.1"
