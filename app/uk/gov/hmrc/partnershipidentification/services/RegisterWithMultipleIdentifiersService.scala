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

package uk.gov.hmrc.partnershipidentification.services

import play.api.libs.json.{JsObject, Json}

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersConnector
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersHttpParser.RegisterWithMultipleIdentifiersResult

import scala.concurrent.Future

@Singleton
class RegisterWithMultipleIdentifiersService @Inject()(registerWithMultipleIdentifiersConnector: RegisterWithMultipleIdentifiersConnector) {

  def registerGeneralPartnership(sautr: String, regime: String)(implicit hc: HeaderCarrier): Future[RegisterWithMultipleIdentifiersResult] = {
    val jsonBody: JsObject =
      Json.obj(
        "ordinaryPartnership" ->
          Json.obj(
            "sautr" -> sautr
          )
      )
    registerWithMultipleIdentifiersConnector.register(jsonBody, regime)
  }

  def registerScottishPartnership(sautr: String, regime: String)(implicit hc: HeaderCarrier): Future[RegisterWithMultipleIdentifiersResult] = {
    val jsonBody: JsObject =
      Json.obj(
        "scottishPartnership" ->
          Json.obj(
            "sautr" -> sautr
          )
      )
    registerWithMultipleIdentifiersConnector.register(jsonBody, regime)
  }

  def registerLimitedPartnership(sautr: String, companyNumber: String, regime: String)(implicit hc: HeaderCarrier): Future[RegisterWithMultipleIdentifiersResult] = {
    val jsonBody: JsObject =
      Json.obj(
        "limitedPartnership" ->
          Json.obj(
            "sautr" -> sautr,
            "crn" -> companyNumber
          )
      )
    registerWithMultipleIdentifiersConnector.register(jsonBody, regime)
  }

  def registerLimitedLiabilityPartnership(sautr: String, companyNumber: String, regime: String)(implicit hc: HeaderCarrier): Future[RegisterWithMultipleIdentifiersResult] = {
    val jsonBody: JsObject =
      Json.obj(
        "limitedLiabilityPartnership" ->
          Json.obj(
            "sautr" -> sautr,
            "crn" -> companyNumber
          )
      )
    registerWithMultipleIdentifiersConnector.register(jsonBody, regime)
  }

  def registerScottishLimitedPartnership(sautr: String, companyNumber: String, regime: String)(implicit hc: HeaderCarrier): Future[RegisterWithMultipleIdentifiersResult] = {
    val jsonBody: JsObject =
      Json.obj(
        "scottishLimitedPartnership" ->
          Json.obj(
            "sautr" -> sautr,
            "crn" -> companyNumber
          )
      )
    registerWithMultipleIdentifiersConnector.register(jsonBody, regime)
  }
}
