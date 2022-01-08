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

package uk.gov.hmrc.partnershipidentification.controllers

import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers.{OK, NOT_FOUND, INTERNAL_SERVER_ERROR}
import uk.gov.hmrc.partnershipidentification.assets.TestConstants._
import uk.gov.hmrc.partnershipidentification.stubs.PartnershipKnownFactsStub
import uk.gov.hmrc.partnershipidentification.utils.ComponentSpecHelper

class ValidatePartnershipInformationControllerISpec extends ComponentSpecHelper with PartnershipKnownFactsStub {

  val testJson: JsObject = Json.obj("sautr" -> testSautr, "postcode" -> testPostcode)

  "POST /validate-partnership-information" should {
    "return 'identifiersMatch: true" when {
      "the data provided matches what is held downstream" in {
        stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> true)
      }
    }

    "return 'identifiersMatch: false" when {
      "the data provided does not match what is held downstream" in {
        val testJson: JsObject = Json.obj("sautr" -> testSautr, "postcode" -> "AB1 1AB")
        stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> false)
      }

      "the downstream endpoint successfully returns no postcodes" in {
        stubGetPartnershipKnownFacts(testSautr)(OK, None)

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> false)
      }

      "the data was not found" in {
        stubGetPartnershipKnownFacts(testSautr)(NOT_FOUND, None)

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe OK
        result.json mustBe Json.obj("identifiersMatch" -> false)
      }
    }

    "throw an exception" when {
      "the downstream endpoint returns an unexpected response" in {
        stubGetPartnershipKnownFacts(testSautr)(INTERNAL_SERVER_ERROR, None)

        val result = post("/validate-partnership-information")(testJson)

        result.status mustBe INTERNAL_SERVER_ERROR
      }
    }
  }

}
