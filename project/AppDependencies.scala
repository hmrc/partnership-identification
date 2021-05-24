import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"                  %% "bootstrap-backend-play-28"  % "5.3.0",
    "uk.gov.hmrc"                  %% "simple-reactivemongo"       % "8.0.0-play-28",
    "com.fasterxml.jackson.module" %% "jackson-module-scala"       % "2.12.3"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % "5.3.0"             % Test,

    "org.scalatest"           %% "scalatest"                  % "3.2.5"             % Test,
    "com.typesafe.play"       %% "play-test"                  % PlayVersion.current % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.36.8"            % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "5.1.0"             % "test, it",
    "com.github.tomakehurst"  %  "wiremock-jre8"              % "2.28.0"            % IntegrationTest,
    "org.mockito"             %% "mockito-scala"              % "1.16.37"           % Test,
    "org.mockito"             %% "mockito-scala-scalatest"    % "1.16.37"           % Test

  )
}
