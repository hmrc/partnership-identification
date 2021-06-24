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

package uk.gov.hmrc.partnershipidentification.connectors

import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.Json
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.partnershipidentification.assets.TestConstants._
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersHttpParser.{RegisterWithMultipleIdentifiersFailure, RegisterWithMultipleIdentifiersSuccess}
import uk.gov.hmrc.partnershipidentification.featureswitch.core.config.{FeatureSwitching, StubRegisterWithIdentifiers}
import uk.gov.hmrc.partnershipidentification.stubs.{AuthStub, RegisterWithMultipleIdentifiersStub}
import uk.gov.hmrc.partnershipidentification.utils.ComponentSpecHelper


class RegisterWithMultipleIdentifiersConnectorISpec extends ComponentSpecHelper with AuthStub with RegisterWithMultipleIdentifiersStub with FeatureSwitching {

  lazy val connector: RegisterWithMultipleIdentifiersConnector = app.injector.instanceOf[RegisterWithMultipleIdentifiersConnector]

  private implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  "registerWithMultipleIdentifiers" when {
    s"the $StubRegisterWithIdentifiers feature switch is disabled" when {
      "return OK with status Registered and the SafeId" when {
        "the Registration was a success on the Register API" in {
          disable(StubRegisterWithIdentifiers)

          stubRegisterWithMultipleIdentifiersSuccess(testSautr)(OK, testSafeId)
          val result = connector.register(testSautr)
          await(result) mustBe (RegisterWithMultipleIdentifiersSuccess(testSafeId))
        }
      }
    }
    s"the $StubRegisterWithIdentifiers feature switch is enabled" when {
      "return OK with status Registered and the SafeId" when {
        "the Registration was a success on the Register API stub" in {
          enable(StubRegisterWithIdentifiers)

          stubRegisterWithMultipleIdentifiersSuccess(testSautr)(OK, testSafeId)
          val result = connector.register(testSautr)
          await(result) mustBe (RegisterWithMultipleIdentifiersSuccess(testSafeId))
        }
      }
    }
    s"the $StubRegisterWithIdentifiers feature switch is enabled" when {
      "return a failure with the Json body" when {
        "the Registration was a failure on the Register API stub" in {
          enable(StubRegisterWithIdentifiers)

          stubRegisterWithMultipleIdentifiersFailure(testSautr)(BAD_REQUEST)
          val result = connector.register(testSautr)
          await(result) mustBe RegisterWithMultipleIdentifiersFailure(BAD_REQUEST, Json.obj(
            "code" -> "INVALID_PAYLOAD",
            "reason" -> "Request has not passed validation. Invalid Payload."
          ).toString())
        }
      }
    }
  }

}
