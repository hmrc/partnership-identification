import sbt.Setting
import scoverage.ScoverageKeys

object ScoverageSettings {
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

  val settings: Seq[Setting[_]] = Seq(
    ScoverageKeys.coverageExcludedPackages := exclusionList.mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 90,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}
