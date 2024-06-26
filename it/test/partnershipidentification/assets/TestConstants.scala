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

package partnershipidentification.assets

import play.api.libs.json.{JsObject, Json}

import java.util.UUID

object TestConstants {

  val testJourneyId: String = UUID.randomUUID().toString
  val testInternalId: String = UUID.randomUUID().toString
  val testRegime = "VATC"
  val testSafeId: String = UUID.randomUUID().toString
  val testSautr: String = "1234567890"
  val testCompanyNumber: String = "12345678"
  val testPostcode: String = "AA1 1AA"
  val testCorrespondencePostCode: String = UUID.randomUUID().toString
  val testBasePostCode: String = UUID.randomUUID().toString
  val testCommsPostCode: String = UUID.randomUUID().toString
  val testTraderPostCode: String = UUID.randomUUID().toString

  val testGeneralPartnershipjsonBody: JsObject =
    Json.obj(
      "ordinaryPartnership" ->
        Json.obj(
          "sautr" -> testSautr
        )
    )

  val invalidPayload: String = "INVALID_PAYLOAD"
  val invalidRegime: String = "INVALID_REGIME"
  val requestFailedValidationPayload: String = "Request has not passed validation. Invalid Payload."
  val requestFailedValidationRegime: String = "Request has not passed validation. Invalid Regime."

  val testSingleErrorRegistrationFailureResponse: JsObject =  Json.obj(
    "code" -> invalidPayload,
    "reason" -> requestFailedValidationPayload
  )

  val testMultipleErrorRegistrationFailureResponses: JsObject = Json.obj(
    "failures" -> Json.arr(
      Json.obj(
        "code" -> invalidRegime,
        "reason" -> requestFailedValidationRegime
      ),
      Json.obj(
        "code" -> invalidPayload,
        "reason" -> requestFailedValidationPayload
      )
    )
  )

}
