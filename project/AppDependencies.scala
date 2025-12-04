import sbt.*

object AppDependencies {

  val bootStrapVersion: String = "10.4.0"
  val HMRCMongoVersion: String = "2.11.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                  %% "bootstrap-backend-play-30"  % bootStrapVersion,
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-play-30"         % HMRCMongoVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala"       % "2.20.1"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % bootStrapVersion,
    "org.scalatest"           %% "scalatest"                  % "3.2.19",
    "org.playframework"       %% "play-test"                  % "3.0.9",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.64.8",
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "7.0.2",
    "org.wiremock"            %  "wiremock"                   % "3.13.2",
    "org.scalatestplus"       %% "mockito-5-18"               % "3.2.19.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"    % HMRCMongoVersion
  ).map(_ % "test")
}
