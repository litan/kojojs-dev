import org.scalajs.jsenv.selenium.SeleniumJSEnv

enablePlugins(ScalaJSPlugin, JSDependenciesPlugin)

name := "Kojo Dev"
scalaVersion := "2.13.18"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "1.2.0",
  "org.scalatest" %%% "scalatest" % "3.2.19" % "test"
)

jsDependencies += ProvidedJS / "pixi.min.js" % "test"
jsDependencies += ProvidedJS / "jsts.min.js" % "test"

val capabilities = new org.openqa.selenium.chrome.ChromeOptions()
Test / jsEnv := new org.scalajs.jsenv.selenium.SeleniumJSEnv(
  capabilities, SeleniumJSEnv.Config().withKeepAlive(false))