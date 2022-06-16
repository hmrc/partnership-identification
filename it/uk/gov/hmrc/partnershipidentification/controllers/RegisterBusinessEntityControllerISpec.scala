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

import play.api.http.Status.{BAD_REQUEST, OK, UNAUTHORIZED}
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.partnershipidentification.assets.TestConstants._
import uk.gov.hmrc.partnershipidentification.stubs.{AuthStub, RegisterWithMultipleIdentifiersStub}
import uk.gov.hmrc.partnershipidentification.utils.ComponentSpecHelper


class RegisterBusinessEntityControllerISpec extends ComponentSpecHelper with AuthStub with RegisterWithMultipleIdentifiersStub {

  val successfulResultAsString: String =
    s"""
       |{
       |  "registration" : {
       |    "registrationStatus" : "REGISTERED",
       |    "registeredBusinessPartnerId" : "$testSafeId"
       |  }
       |}""".stripMargin

  val singleFailureResultAsString: String =
    s"""
       |{
       |  "registration" : {
       |    "registrationStatus" : "REGISTRATION_FAILED",
       |    "failures" : [
       |      {
       |        "code" : "$invalidPayload",
       |        "reason" : "$requestFailedValidationPayload"
       |      }
       |    ]
       |  }
       |}""".stripMargin

  val multipleFailureResultAsString: String =
    s"""
       |{
       |  "registration" : {
       |    "registrationStatus" : "REGISTRATION_FAILED",
       |    "failures" : [
       |      {
       |        "code" : "$invalidRegime",
       |        "reason" : "$requestFailedValidationRegime"
       |      },
       |      {
       |        "code" : "$invalidPayload",
       |        "reason" : "$requestFailedValidationPayload"
       |      }
       |    ]
       |  }
       |}""".stripMargin

  val successfulRegistrationAsJson: JsObject = Json.parse(successfulResultAsString).as[JsObject]
  val singleFailureResultAsJson: JsObject = Json.parse(singleFailureResultAsString).as[JsObject]
  val multipleFailureResultAsJson: JsObject = Json.parse(multipleFailureResultAsString).as[JsObject]

  "POST /register-general-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterGeneralPartnershipWithMultipleIdentifiersSuccess(testSautr, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "regime" -> testRegime
        )

        val result = post("/register-general-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe successfulRegistrationAsJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(testSautr, testRegime)(BAD_REQUEST, testSingleErrorRegistrationFailureResponse)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "regime" -> testRegime
        )

        val result = post("/register-general-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe singleFailureResultAsJson

      }
    }
    "return Unauthorised" when {
      "there is an auth failure" in {
        stubAuthFailure()
        stubRegisterGeneralPartnershipWithMultipleIdentifiersSuccess(testSautr, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "regime" -> testRegime
        )

        val result = post("/register-general-partnership")(jsonBody)

        result.status mustBe UNAUTHORIZED
      }
    }
  }
  "POST /register-scottish-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterScottishPartnershipWithMultipleIdentifiersSuccess(testSautr, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "regime" -> testRegime
        )

        val result = post("/register-scottish-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe successfulRegistrationAsJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterScottishPartnershipWithMultipleIdentifiersFailure(testSautr, testRegime)(BAD_REQUEST, testMultipleErrorRegistrationFailureResponses)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "regime" -> testRegime
        )

        val result = post("/register-scottish-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe multipleFailureResultAsJson
      }
    }
    "return Unauthorised" when {
      "there is an auth failure" in {
        stubAuthFailure()
        stubRegisterScottishPartnershipWithMultipleIdentifiersSuccess(testSautr, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "regime" -> testRegime
        )

        val result = post("/register-scottish-partnership")(jsonBody)

        result.status mustBe UNAUTHORIZED
      }
    }
  }
  "POST /register-limited-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterLimitedPartnershipWithMultipleIdentifiersSuccess(testSautr, testCompanyNumber, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-limited-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe successfulRegistrationAsJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterLimitedPartnershipWithMultipleIdentifiersFailure(
          testSautr, testCompanyNumber, testRegime)(BAD_REQUEST, testSingleErrorRegistrationFailureResponse)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-limited-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe singleFailureResultAsJson
      }
    }
    "return Unauthorised" when {
      "there is an auth failure" in {
        stubAuthFailure()
        stubRegisterLimitedPartnershipWithMultipleIdentifiersSuccess(testSautr, testCompanyNumber, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-limited-partnership")(jsonBody)

        result.status mustBe UNAUTHORIZED
      }
    }
  }
  "POST /register-limited-liability-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterLimitedLiabilityPartnershipWithMultipleIdentifiersSuccess(testSautr, testCompanyNumber, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-limited-liability-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe successfulRegistrationAsJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterLimitedLiabilityPartnershipWithMultipleIdentifiersFailure(
          testSautr, testCompanyNumber, testRegime)(BAD_REQUEST, testSingleErrorRegistrationFailureResponse)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-limited-liability-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe singleFailureResultAsJson
      }
    }
    "return Unauthorised" when {
      "there is an auth failure" in {
        stubAuthFailure()
        stubRegisterLimitedLiabilityPartnershipWithMultipleIdentifiersSuccess(testSautr, testCompanyNumber, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-limited-liability-partnership")(jsonBody)

        result.status mustBe UNAUTHORIZED
      }
    }
  }
  "POST /register-scottish-limited-partnership" should {
    "return OK with status Registered and the SafeId" when {
      "the Registration was a success" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterScottishLimitedPartnershipWithMultipleIdentifiersSuccess(testSautr, testCompanyNumber, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-scottish-limited-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe successfulRegistrationAsJson
      }
    }
    "return OK with status Registration_Failed" when {
      "the Registration was not successful" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        stubRegisterScottishLimitedPartnershipWithMultipleIdentifiersFailure(
          testSautr, testCompanyNumber, testRegime)(BAD_REQUEST, testMultipleErrorRegistrationFailureResponses)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-scottish-limited-partnership")(jsonBody)
        result.status mustBe OK
        result.json mustBe multipleFailureResultAsJson
      }
    }
    "return Unauthorised" when {
      "there is an auth failure" in {
        stubAuthFailure()
        stubRegisterScottishLimitedPartnershipWithMultipleIdentifiersSuccess(testSautr, testCompanyNumber, testRegime)(OK, testSafeId)

        val jsonBody = Json.obj(
          "sautr" -> testSautr,
          "companyNumber" -> testCompanyNumber,
          "regime" -> testRegime
        )

        val result = post("/register-scottish-limited-partnership")(jsonBody)

        result.status mustBe UNAUTHORIZED
      }
    }
  }
}
