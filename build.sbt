import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import scoverage.ScoverageKeys

val appName = "partnership-identification"

val silencerVersion = "1.7.3"

lazy val scoverageSettings = {

  val exclusionList: List[String] = List(
    "<empty>",
    ".*Routes.*",
    ".*Reverse.*",
    "app.*",
    "prod.*",
    "com.kenshoo.play.metrics.*",
    "uk.gov.hmrc.partnershipidentification.featureswitch.api.*",
    "uk.gov.hmrc.partnershipidentification.testonly"
  )

  Seq(
    ScoverageKeys.coverageExcludedPackages := exclusionList.mkString(";"),
    ScoverageKeys.coverageMinimum := 90,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .settings(
    majorVersion                     := 0,
    scalaVersion                     := "2.12.13",
    libraryDependencies              ++= AppDependencies.compile ++ AppDependencies.test,
    // ***************
    // Use the silencer plugin to suppress warnings
    scalacOptions += "-P:silencer:pathFilters=routes",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
    )
    // ***************
  )
  .settings(scoverageSettings)
  .settings(publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(calculateITTestsGroupingSettings(System.getProperty("isADevMachine")): _*)
  .settings(resolvers += Resolver.jcenterRepo)

def calculateITTestsGroupingSettings(isADevMachineProperty: String): Seq[sbt.Setting[_]] = {
  IntegrationTest / testGrouping := {
    if ("true".equals(isADevMachineProperty))
      onlyOneJvmForAllISpecTestsTestGroup.value
    else
      (IntegrationTest / testGrouping).value
  }
}

lazy val onlyOneJvmForAllISpecTestsTestGroup = taskKey[Seq[Tests.Group]]("Default test group that run all the tests in only one JVM - (much faster!)")

onlyOneJvmForAllISpecTestsTestGroup := Seq(new Tests.Group(
  "<default>",
  (IntegrationTest / definedTests).value,
  Tests.InProcess,
  Seq.empty
))

