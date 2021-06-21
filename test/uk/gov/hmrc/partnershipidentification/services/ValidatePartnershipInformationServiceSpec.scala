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

package uk.gov.hmrc.partnershipidentification.services

import helpers.TestConstants._
import org.mockito.scalatest.{IdiomaticMockito, ResetMocksAfterEachTest}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.partnershipidentification.connectors.PartnershipKnownFactsConnector
import uk.gov.hmrc.partnershipidentification.models.PartnershipKnownFacts
import uk.gov.hmrc.partnershipidentification.services.ValidatePartnershipInformationService.{PostCodeDoesNotMatch, PostCodeMatched}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ValidatePartnershipInformationServiceSpec extends AnyWordSpec with Matchers with IdiomaticMockito with ResetMocksAfterEachTest {
  implicit val hc: HeaderCarrier = HeaderCarrier()


  val mockKnownFactsConnector: PartnershipKnownFactsConnector = mock[PartnershipKnownFactsConnector]

  object TestJourneyDataService extends ValidatePartnershipInformationService(mockKnownFactsConnector)


  "validate" should {
    "return a matched response" when {
      "the data exists in the database" in {

        mockKnownFactsConnector.getPartnershipKnownFacts(testSautr) returns Future(PartnershipKnownFacts(
          postCode = Some(testPostcode),
          correspondencePostCode = Some(testCorrespondencePostCode),
          basePostCode = Some(testBasePostCode),
          commsPostCode = Some(testCommsPostCode),
          traderPostCode = Some(testTraderPostCode)
        ))

        await(TestJourneyDataService.validate(testSautr, testPostcode)) mustBe Right(PostCodeMatched)

      }
    }
    "return an unmatched response" when {
      "the data does not exist in the database" in {

        mockKnownFactsConnector.getPartnershipKnownFacts(testSautr) returns Future(PartnershipKnownFacts(
          None, None, None, None, None
        ))

        await(TestJourneyDataService.validate(testSautr, testPostcode)) mustBe Left(PostCodeDoesNotMatch)
      }
    }
  }
}
