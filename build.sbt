enablePlugins(ScalaJSPlugin)

name := "Kojo Dev"
scalaVersion := "2.12.8"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"