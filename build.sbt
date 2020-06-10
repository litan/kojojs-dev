import org.scalajs.jsenv.selenium.SeleniumJSEnv

enablePlugins(ScalaJSPlugin)

name := "Kojo Dev"
scalaVersion := "2.12.10"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.8",
  "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
)

jsDependencies += ProvidedJS / "pixi.min.js" % "test"
jsDependencies += ProvidedJS / "jsts.min.js" % "test"

val capabilities = new org.openqa.selenium.chrome.ChromeOptions()
jsEnv in Test := new org.scalajs.jsenv.selenium.SeleniumJSEnv(
  capabilities, SeleniumJSEnv.Config().withKeepAlive(false))