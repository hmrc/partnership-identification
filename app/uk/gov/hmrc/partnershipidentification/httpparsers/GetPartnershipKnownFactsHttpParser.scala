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

package uk.gov.hmrc.partnershipidentification.httpparsers

import play.api.http.Status._
import play.api.libs.json.{JsError, JsSuccess}
import uk.gov.hmrc.http.{HttpReads, HttpResponse, InternalServerException}
import uk.gov.hmrc.partnershipidentification.models.PartnershipKnownFacts

object GetPartnershipKnownFactsHttpParser {

  val postCodeKey = "postCode"

  val correspondenceDetailsKey = "correspondenceDetails"
  val correspondencePostCodeKey = "correspondencePostCode"

  val basePostCodeKey = "basePostCode"

  val commsPostCodeKey = "commsPostCode"

  val traderPostCodeKey = "traderPostCode"

  implicit object GetPartnershipKnownFactsHttpReads extends HttpReads[PartnershipKnownFacts] {
    override def read(method: String, url: String, response: HttpResponse): PartnershipKnownFacts =
      response.status match {
        case OK => {
          for {
            postCode <- (response.json \ postCodeKey).validateOpt[String]
            correspondencePostCode <- (response.json \ correspondenceDetailsKey \ correspondencePostCodeKey).validateOpt[String]
            baseTaxpayerPostCode <- (response.json \ basePostCodeKey).validateOpt[String]
            commsPostCode <- (response.json \ commsPostCodeKey).validateOpt[String]
            traderPostCode <- (response.json \ traderPostCodeKey).validateOpt[String]
          } yield PartnershipKnownFacts(
            postCode = postCode,
            correspondencePostCode = correspondencePostCode,
            basePostCode = baseTaxpayerPostCode,
            commsPostCode = commsPostCode,
            traderPostCode = traderPostCode
          )
        } match {
          case JsSuccess(partnershipKnownFacts, _) =>
            partnershipKnownFacts
          case JsError(errors) =>
            throw new InternalServerException(s"getPartnershipKnownFacts API returned invalid JSON with errors: $errors")
        }
        case NOT_FOUND =>
          PartnershipKnownFacts(None, None, None, None, None)
        case status =>
          throw new InternalServerException(s"getPartnershipKnownFacts API returned an unexpected status: $status, body: ${response.body}")
      }
  }

}
