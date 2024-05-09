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

package partnershipidentification.controllers

import play.api.http.Status.UNAUTHORIZED
import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers.{INTERNAL_SERVER_ERROR, NOT_FOUND, OK}
import partnershipidentification.stubs.{AuthStub, PartnershipKnownFactsStub}
import partnershipidentification.utils.ComponentSpecHelper
import partnershipidentification.assets.TestConstants._

class ValidatePartnershipInformationControllerISpec extends ComponentSpecHelper with PartnershipKnownFactsStub with AuthStub {

  val testJson: JsObject = Json.obj("sautr" -> testSautr, "postcode" -> testPostcode)

  "POST /validate-partnership-information" should {
    "return 'identifiersMatch: true" when {
      "the data provided matches what is held downstream" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> true)
      }
    }

    "return 'identifiersMatch: false" when {
      "the data provided does not match what is held downstream" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        val testJson: JsObject = Json.obj("sautr" -> testSautr, "postcode" -> "AB1 1AB")
        stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> false)
      }

      "the downstream endpoint successfully returns no postcodes" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubGetPartnershipKnownFacts(testSautr)(OK, None)

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> false)
      }

      "the data was not found" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubGetPartnershipKnownFacts(testSautr)(NOT_FOUND, None)

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> false)
      }

      "return Unauthorised" when {
        "there is an auth failure" in {
          stubAuthFailure()
          stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

          val result = post("/validate-partnership-information")(testJson)

          result.status mustBe UNAUTHORIZED

        }
      }
    }

    "throw an exception" when {
      "the downstream endpoint returns an unexpected response" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubGetPartnershipKnownFacts(testSautr)(INTERNAL_SERVER_ERROR, None)

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

}
