/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.partnershipidentification.config

import play.api.Configuration
import uk.gov.hmrc.partnershipidentification.featureswitch.core.config.{FeatureSwitching, StubPartnershipKnownFacts, StubRegisterWithIdentifiers}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}

@Singleton
class AppConfig @Inject()(config: Configuration,
                          servicesConfig: ServicesConfig) extends FeatureSwitching {

  val authBaseUrl: String = servicesConfig.baseUrl("auth")

  val auditingEnabled: Boolean = config.get[Boolean]("auditing.enabled")
  val graphiteHost: String = config.get[String]("microservice.metrics.graphite.host")

  lazy val timeToLiveSeconds: Long = servicesConfig.getString("mongodb.timeToLiveSeconds").toLong

  lazy val desBaseUrl: String = servicesConfig.getString("microservice.services.des.url")

  lazy val desStubBaseUrl: String = servicesConfig.getString("microservice.services.des.stub-url")

  lazy val desEnvironmentHeader: (String, String) =
    "Environment" -> servicesConfig.getString("microservice.services.des.environment")

  lazy val desAuthorisationToken: String = s"Bearer ${servicesConfig.getString("microservice.services.des.authorisation-token")}"

  def getPartnershipKnownFactsUrl(sautr: String): String = {
    val baseUrl: String = if(isEnabled(StubPartnershipKnownFacts)) desStubBaseUrl else desBaseUrl
    s"$baseUrl/income-tax-self-assessment/known-facts/utr/$sautr"
  }
  def getRegisterWithMultipleIdentifiersUrl: String = {
    val baseUrl = if (isEnabled(StubRegisterWithIdentifiers)) desStubBaseUrl else desBaseUrl
    s"$baseUrl/cross-regime/register/VATC"
  }

}
