import sbt.*

object AppDependencies {

  val bootStrapVersion: String = "9.11.0"
  val HMRCMongoVersion: String = "2.6.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                  %% "bootstrap-backend-play-30"  % bootStrapVersion,
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-play-30"         % HMRCMongoVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala"       % "2.18.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % bootStrapVersion     % Test,
    "org.scalatest"           %% "scalatest"                  % "3.2.19"             % Test,
    "org.playframework"       %% "play-test"                  % "3.0.4"              % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.8"             % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "7.0.1"              % Test,
    "org.wiremock"            %  "wiremock"                   % "3.9.1"              % Test,
    "org.mockito"             %% "mockito-scala"              % "1.17.37"            % Test,
    "org.mockito"             %% "mockito-scala-scalatest"    % "1.17.37"            % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % HMRCMongoVersion     % Test
  )
}
