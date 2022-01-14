/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.partnershipidentification.connectors

import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import uk.gov.hmrc.partnershipidentification.config.AppConfig
import uk.gov.hmrc.partnershipidentification.connectors.PartnershipKnownFactsConnector._
import uk.gov.hmrc.partnershipidentification.httpparsers.GetPartnershipKnownFactsHttpParser._
import uk.gov.hmrc.partnershipidentification.models.PartnershipKnownFacts

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PartnershipKnownFactsConnector @Inject()(http: HttpClient,
                                               appConfig: AppConfig)(implicit ec: ExecutionContext) {


  def getPartnershipKnownFacts(sautr: String)
                              (implicit hc: HeaderCarrier): Future[PartnershipKnownFacts] = {

    val extraHeaders = Seq(
      "Authorization" -> appConfig.desAuthorisationToken,
      appConfig.desEnvironmentHeader
    )

    http.GET(
      url = appConfig.getPartnershipKnownFactsUrl(sautr: String),
      queryParams = Seq(ReturnTypeKey -> PartnershipReturnType),
      headers = extraHeaders
    )

  }
}

object PartnershipKnownFactsConnector {
  val ReturnTypeKey: String = "returnType"
  val PartnershipReturnType: String = "P"
}
