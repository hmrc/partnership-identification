import sbt.*

object AppDependencies {

  val bootStrapVersion: String = "8.5.0"
  val HMRCMongoVersion: String = "1.8.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                  %% "bootstrap-backend-play-30"  % bootStrapVersion,
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-play-30"         % HMRCMongoVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala"       % "2.17.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % bootStrapVersion    % Test,
    "org.scalatest"           %% "scalatest"                  % "3.2.18"             % Test,
    "com.typesafe.play"       %% "play-test"                  % "2.9.2"             % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.8"            % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "7.0.1"             % Test,
    "com.github.tomakehurst"  %  "wiremock-jre8"              % "3.0.1"            % Test,
    "org.mockito"             %% "mockito-scala"              % "1.17.31"           % Test,
    "org.mockito"             %% "mockito-scala-scalatest"    % "1.17.31"           % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % HMRCMongoVersion         % Test
  )
}
