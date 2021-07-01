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

package uk.gov.hmrc.partnershipidentification.controllers

import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.Json
import uk.gov.hmrc.partnershipidentification.assets.TestConstants._
import uk.gov.hmrc.partnershipidentification.stubs.{AuthStub, RegisterWithMultipleIdentifiersStub}
import uk.gov.hmrc.partnershipidentification.utils.ComponentSpecHelper


class RegisterBusinessEntityControllerISpec extends ComponentSpecHelper with AuthStub with RegisterWithMultipleIdentifiersStub {

  "POST /register-general-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterGeneralPartnershipWithMultipleIdentifiersSuccess(testSautr)(OK, testSafeId)

        val jsonBody = Json.obj(
          "ordinaryPartnership" -> Json.obj(
            "sautr" -> testSautr
          )
        )

        val resultJson = Json.obj(
          "registration" -> Json.obj(
            "registrationStatus" -> "REGISTERED",
            "registeredBusinessPartnerId" -> testSafeId))

        val result = post("/register-general-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe resultJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(testSautr)(BAD_REQUEST)

        val jsonBody = Json.obj(
          "ordinaryPartnership" -> Json.obj(
            "sautr" -> testSautr
          )
        )
        val resultJson = Json.obj(
          "registration" -> Json.obj(
            "registrationStatus" -> "REGISTRATION_FAILED"))

        val result = post("/register-general-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe resultJson

      }
    }
  }
  "POST /register-scottish-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterScottishPartnershipWithMultipleIdentifiersSuccess(testSautr)(OK, testSafeId)

        val jsonBody = Json.obj(
          "scottishPartnership" -> Json.obj(
            "sautr" -> testSautr
          )
        )

        val resultJson = Json.obj(
          "registration" -> Json.obj(
            "registrationStatus" -> "REGISTERED",
            "registeredBusinessPartnerId" -> testSafeId))

        val result = post("/register-scottish-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe resultJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterScottishPartnershipWithMultipleIdentifiersFailure(testSautr)(BAD_REQUEST)

        val jsonBody = Json.obj(
          "scottishPartnership" -> Json.obj(
            "sautr" -> testSautr
          )
        )
        val resultJson = Json.obj(
          "registration" -> Json.obj(
            "registrationStatus" -> "REGISTRATION_FAILED"))

        val result = post("/register-scottish-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe resultJson

      }
    }
  }
}
