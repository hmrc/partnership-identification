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

package partnershipidentification.repositories

import java.util.UUID
import play.api.libs.json.{JsString, Json}
import play.api.test.Helpers._
import partnershipidentification.utils.ComponentSpecHelper
import partnershipidentification.assets.TestConstants.{testInternalId, testJourneyId}
import uk.gov.hmrc.partnershipidentification.repositories.JourneyDataRepository

class JourneyDataRepositoryISpec extends ComponentSpecHelper {

  val repo: JourneyDataRepository = app.injector.instanceOf[JourneyDataRepository]

  override def beforeEach(): Unit = {
    super.beforeEach()
    await(repo.drop)
  }

  val journeyIdKey: String = "_id"
  val authInternalIdKey: String = "authInternalId"
  val creationTimestampKey: String = "creationTimestamp"

  "createJourney" should {
    "successfully insert the journeyId" in {
      await(repo.createJourney(testJourneyId, testInternalId)) mustBe testJourneyId
    }
  }

  s"getJourneyData($testJourneyId)" should {
    "successfully return all data" in {
      await(repo.createJourney(testJourneyId, testInternalId))
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(_.-(creationTimestampKey)) mustBe
        Some(Json.obj(journeyIdKey -> testJourneyId, authInternalIdKey -> testInternalId))
    }
  }

  "updateJourneyData" should {
    "successfully insert data" in {

      val testKey = "testKey"
      val testData = "test"

      await(repo.createJourney(testJourneyId, testInternalId))
      await(repo.updateJourneyData(testJourneyId, testKey, JsString(testData), testInternalId)) mustBe true
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(json => (json \ testKey).as[String]) mustBe Some(testData)
    }
    "successfully update data when data is already stored against a key" in {
      val testKey = "testKey"
      val testData = "test"
      val updatedData = "updated"
      await(repo.createJourney(testJourneyId, testInternalId))
      await(repo.updateJourneyData(testJourneyId, testKey, JsString(testData), testInternalId))
      await(repo.updateJourneyData(testJourneyId, testKey, JsString(updatedData), testInternalId)) mustBe true
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(json => (json \ testKey).as[String]) mustBe Some(updatedData)
    }
  }
  "removeJourneyDataField" should {
    "successfully remove a field" in {
      val testKey = "testKey"
      val testData = "test"

      await(repo.createJourney(testJourneyId, testInternalId))
      await(repo.updateJourneyData(testJourneyId, testKey, JsString(testData), testInternalId))
      await(repo.removeJourneyDataField(testJourneyId, testInternalId, testKey)) mustBe true
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(_.keys.contains(creationTimestampKey)) mustBe Some(true)
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(_.-(creationTimestampKey)) mustBe
        Some(Json.obj(journeyIdKey -> testJourneyId, authInternalIdKey -> testInternalId))
    }
    "pass successfully when the field is not present" in {
      val testKey = "testKey"
      val testData = "test"
      val testSecondKey = "secondKey"

      await(repo.createJourney(testJourneyId, testInternalId))
      await(repo.updateJourneyData(testJourneyId, testKey, JsString(testData), testInternalId))
      await(repo.removeJourneyDataField(testJourneyId, testInternalId, testSecondKey)) mustBe true
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(_.keys.contains(creationTimestampKey)) mustBe Some(true)
      await(repo.getJourneyData(testJourneyId, testInternalId)).map(_.-(creationTimestampKey)) mustBe
        Some(Json.obj(journeyIdKey -> testJourneyId, authInternalIdKey -> testInternalId, testKey -> testData))
    }
    "return false when an incorrect journey id is used" in {
      val testKey = "testKey"
      val testData = "test"

      val newJourneyId: String = UUID.randomUUID().toString

      await(repo.createJourney(testJourneyId, testInternalId))
      await(repo.updateJourneyData(testJourneyId, testKey, JsString(testData), testInternalId))

      await(repo.removeJourneyDataField(newJourneyId, testInternalId, testKey)) mustBe false
    }

  }

}


