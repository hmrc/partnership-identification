import uk.gov.hmrc.DefaultBuildSettings
import play.sbt.PlayImport.PlayKeys.playDefaultPort

val appName = "partnership-identification"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test)
  .settings(
    ThisBuild / majorVersion := 0,
    ThisBuild / scalaVersion := "2.13.16")
  .settings(scalacOptions ++= Seq(
    "-Wconf:src=routes/.*:s",
    "-Wconf:cat=unused-imports&src=html/.*:s"
  ))
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(ScoverageSettings.settings *)
  .settings(playDefaultPort := 9987)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings())
