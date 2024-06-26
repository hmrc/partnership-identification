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

import play.api.test.Helpers._
import partnershipidentification.stubs.PartnershipKnownFactsStub
import partnershipidentification.utils.ComponentSpecHelper
import uk.gov.hmrc.http.{HeaderCarrier, InternalServerException}
import partnershipidentification.assets.TestConstants._
import uk.gov.hmrc.partnershipidentification.connectors.PartnershipKnownFactsConnector
import uk.gov.hmrc.partnershipidentification.featureswitch.core.config.{FeatureSwitching, StubPartnershipKnownFacts}
import uk.gov.hmrc.partnershipidentification.models.PartnershipKnownFacts

class PartnershipKnownFactsConnectorISpec extends ComponentSpecHelper with PartnershipKnownFactsStub with FeatureSwitching{

  private lazy val knownFactsConnector: PartnershipKnownFactsConnector =
    app.injector.instanceOf[PartnershipKnownFactsConnector]


  private implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  "getPartnershipKnownFacts" when {
    s"the $StubPartnershipKnownFacts feature switch is disabled" when {
      "DES returns a successful response" should {
        "return PartnershipKnownFacts" in {
          disable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

          val result = await(knownFactsConnector.getPartnershipKnownFacts(testSautr))

          result mustBe PartnershipKnownFacts(
            postCode = Some(testPostcode),
            correspondencePostCode = Some(testCorrespondencePostCode),
            basePostCode = Some(testBasePostCode),
            commsPostCode = Some(testCommsPostCode),
            traderPostCode = Some(testTraderPostCode)
          )
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is disabled" when {
      "DES returns a successful response with no postcodes" should {
        "return PartnershipKnownFacts with no postcodes" in {
          disable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(OK, None)

          val result = await(knownFactsConnector.getPartnershipKnownFacts(testSautr))

          result mustBe PartnershipKnownFacts(None, None, None, None, None)
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is disabled" when {
      "DES returns Not Found" should {
        "return PartnershipKnownFacts with no postcodes" in {
          enable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(NOT_FOUND, None)

          val result = await(knownFactsConnector.getPartnershipKnownFacts(testSautr))

          result mustBe PartnershipKnownFacts(None, None, None, None, None)
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is disabled" when {
      "DES returns an unexpected response" should {
        "throw an exception" in {
          disable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(INTERNAL_SERVER_ERROR, None)

          intercept[InternalServerException](await(knownFactsConnector.getPartnershipKnownFacts(testSautr)))
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is enabled" when {
      "DES returns a successful response" should {
        "return PartnershipKnownFacts" in {
          enable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(OK, Some(fullPartnershipKnownFactsBody))

          val result = await(knownFactsConnector.getPartnershipKnownFacts(testSautr))

          result mustBe PartnershipKnownFacts(
            postCode = Some(testPostcode),
            correspondencePostCode = Some(testCorrespondencePostCode),
            basePostCode = Some(testBasePostCode),
            commsPostCode = Some(testCommsPostCode),
            traderPostCode = Some(testTraderPostCode)
          )
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is enabled" when {
      "DES returns a successful response with no postcodes" should {
        "return PartnershipKnownFacts with no postcodes" in {
          enable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(OK, None)

          val result = await(knownFactsConnector.getPartnershipKnownFacts(testSautr))

          result mustBe PartnershipKnownFacts(None, None, None, None, None)
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is enabled" when {
      "DES returns Not Found" should {
        "return PartnershipKnownFacts with no postcodes" in {
          enable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(NOT_FOUND, None)

          val result = await(knownFactsConnector.getPartnershipKnownFacts(testSautr))

          result mustBe PartnershipKnownFacts(None, None, None, None, None)
        }
      }
    }
    s"the $StubPartnershipKnownFacts feature switch is enabled" when {
      "DES returns an unexpected response" should {
        "throw an exception" in {
          enable(StubPartnershipKnownFacts)
          stubGetPartnershipKnownFacts(testSautr)(INTERNAL_SERVER_ERROR, None)

          intercept[InternalServerException](await(knownFactsConnector.getPartnershipKnownFacts(testSautr)))
        }
      }
    }
  }
}
