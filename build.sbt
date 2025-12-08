import uk.gov.hmrc.DefaultBuildSettings
import play.sbt.PlayImport.PlayKeys.playDefaultPort

val appName = "partnership-identification"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test)
  .settings(
    ThisBuild / majorVersion := 0,
    ThisBuild / scalaVersion := "3.7.4")
  .settings(scalacOptions ++= Seq(
    "-Wconf:src=routes/.*:s",
    "-Wconf:msg=unused import&src=html/.*:s",
    "-Wconf:msg=Flag.*repeatedly:s",
    "-Wconf:msg=Implicit parameters should be provided with a `using` clause:s"
  ))
  .settings(ScoverageSettings.settings *)
  .settings(playDefaultPort := 9987)
  .settings(
      Test/javaOptions ++=Seq(
          "--add-opens=java.base/java.lang=ALL-UNNAMED",
          "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
          "-XX:+EnableDynamicAgentLoading",
      )
  )

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings())
