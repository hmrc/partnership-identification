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

package partnershipidentification.connectors

import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import partnershipidentification.stubs.{AuthStub, RegisterWithMultipleIdentifiersStub}
import partnershipidentification.utils.ComponentSpecHelper
import uk.gov.hmrc.http.HeaderCarrier
import partnershipidentification.assets.TestConstants._
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersConnector
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersHttpParser._
import uk.gov.hmrc.partnershipidentification.featureswitch.core.config.{FeatureSwitching, StubRegisterWithIdentifiers}


class RegisterWithMultipleIdentifiersConnectorISpec extends ComponentSpecHelper with AuthStub with RegisterWithMultipleIdentifiersStub with FeatureSwitching {

  lazy val connector: RegisterWithMultipleIdentifiersConnector = app.injector.instanceOf[RegisterWithMultipleIdentifiersConnector]

  private implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  "registerWithMultipleIdentifiers" when {
    s"the $StubRegisterWithIdentifiers feature switch is disabled" when {
      "return OK with status Registered and the SafeId" when {
        "the Registration was a success on the Register API" in {
          disable(StubRegisterWithIdentifiers)

          stubRegisterGeneralPartnershipWithMultipleIdentifiersSuccess(testSautr, testRegime)(OK, testSafeId)
          val result = connector.register(testGeneralPartnershipjsonBody, testRegime)
          await(result) mustBe RegisterWithMultipleIdentifiersSuccess(testSafeId)
        }
      }
    }
    s"the $StubRegisterWithIdentifiers feature switch is disabled" when {
      "return a single failure with the Json body" when {
        "the Registration was a failure on the Register API" in {
          disable(StubRegisterWithIdentifiers)

          stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(testSautr, testRegime)(BAD_REQUEST, testSingleErrorRegistrationFailureResponse)
          await(connector.register(testGeneralPartnershipjsonBody, testRegime)) match {
            case RegisterWithMultipleIdentifiersFailure(status, failures) =>
              status mustBe BAD_REQUEST
              failures.length mustBe 1
              failures.head mustBe Failure(invalidPayload, requestFailedValidationPayload)
            case _ => fail("Registration single failure result expected")
          }
        }
      }
      "return multiple failures with the Json body" when {
        "the Registration was a failure on the Register API" in {
          disable(StubRegisterWithIdentifiers)

          stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(testSautr, testRegime)(BAD_REQUEST, testMultipleErrorRegistrationFailureResponses)

          await(connector.register(testGeneralPartnershipjsonBody, testRegime)) match {
            case RegisterWithMultipleIdentifiersFailure(status, failures) =>
              status mustBe BAD_REQUEST
              failures.length mustBe 2
              failures.head mustBe Failure(invalidRegime, requestFailedValidationRegime)
              failures.last mustBe Failure(invalidPayload, requestFailedValidationPayload)
            case _ => fail("Registration multiple failure result expected")
          }
        }
      }
    }
    s"the $StubRegisterWithIdentifiers feature switch is enabled" when {
      "return OK with status Registered and the SafeId" when {
        "the Registration was a success on the Register API stub" in {
          enable(StubRegisterWithIdentifiers)

          stubRegisterGeneralPartnershipWithMultipleIdentifiersSuccess(testSautr, testRegime)(OK, testSafeId)
          val result = connector.register(testGeneralPartnershipjsonBody, testRegime)
          await(result) mustBe RegisterWithMultipleIdentifiersSuccess(testSafeId)
        }
      }
    }
    s"the $StubRegisterWithIdentifiers feature switch is enabled" when {
      "return a single failure with the Json body" when {
        "the Registration was a failure on the Register API stub" in {
          enable(StubRegisterWithIdentifiers)

          stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(testSautr, testRegime)(BAD_REQUEST, testSingleErrorRegistrationFailureResponse)
          await(connector.register(testGeneralPartnershipjsonBody, testRegime)) match {
            case RegisterWithMultipleIdentifiersFailure(status, failures) =>
              status mustBe BAD_REQUEST
              failures.length mustBe 1
              failures.head mustBe Failure(invalidPayload, requestFailedValidationPayload)
            case _ => fail("Registration single failure result expected")
          }
        }
      }
      "return multiple failures with the Json body" when {
        "the Registration was a failure on the RegisterAPI stub" in {
          enable(StubRegisterWithIdentifiers)

          stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(testSautr, testRegime)(BAD_REQUEST, testMultipleErrorRegistrationFailureResponses)

          await(connector.register(testGeneralPartnershipjsonBody, testRegime)) match {
            case RegisterWithMultipleIdentifiersFailure(status, failures) =>
              status mustBe BAD_REQUEST
              failures.length mustBe 2
              failures.head mustBe Failure(invalidRegime, requestFailedValidationRegime)
              failures.last mustBe Failure(invalidPayload, requestFailedValidationPayload)
            case _ => fail("Registration single failure result expected")
          }
        }
      }
    }
  }

}
