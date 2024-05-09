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

import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsString, Json}
import play.api.test.Helpers._
import partnershipidentification.stubs.{AuthStub, FakeJourneyIdGenerationService}
import partnershipidentification.utils.{ComponentSpecHelper, CustomMatchers, JourneyDataMongoHelper}
import partnershipidentification.assets.TestConstants.testInternalId
import uk.gov.hmrc.partnershipidentification.services.JourneyIdGenerationService

class JourneyDataControllerISpec extends ComponentSpecHelper with CustomMatchers with JourneyDataMongoHelper with AuthStub {
  lazy val testJourneyId = "testJourneyId"
  lazy val testIncorrectAuthInternalId = "testIncorrectAuthInternalId"

  override lazy val app: Application = new GuiceApplicationBuilder()
    .overrides(bind[JourneyIdGenerationService].toInstance(new FakeJourneyIdGenerationService(testJourneyId)))
    .configure(config)
    .build()

  "POST /journey " when {
    "a new journey begins" should {
      "return OK with the newly generated journey ID" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        val res = post("/journey")(Json.obj())

        res.status mustBe CREATED
        (res.json \ "journeyId").as[String] mustBe testJourneyId
        findById(testJourneyId, testInternalId).map(_.-("creationTimestamp")) mustBe Some(Json.obj("_id" -> testJourneyId, "authInternalId" -> testInternalId))
      }
      "return Unauthorised" in {
        stubAuthFailure()
        val res = post("/journey")(Json.obj())

        res.status mustBe UNAUTHORIZED
      }
    }
  }

  "GET /journey/:journeyId" when {
    "there is data stored against the journey ID" should {
      "return all data stored against the journey ID" in {
        val testData = Json.obj(
          "testField" -> "testValue"
        )

        insertById(testJourneyId, testInternalId, testData)

        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val expectedData = Json.obj(
          "_id" -> testJourneyId,
          "authInternalId" -> testInternalId,
        "testField" -> "testValue"
        )

        val res = get(s"/journey/$testJourneyId")

        res.status mustBe OK
        res.json mustBe expectedData
      }
    }

    "there is no data stored against the journey ID" should {
      "return NOT_FOUND with a code explaining that no data can be found" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        val res = get(s"/journey/$testJourneyId")

        res.status mustBe NOT_FOUND
        res.json mustBe Json.obj(
          "code" -> "NOT_FOUND",
          "reason" -> s"No data exists for journey ID: $testJourneyId"
        )
      }
    }
    "the user cannot be authorised" should {
      "return Unauthorised" in {
        stubAuthFailure()

        val res = get(s"/journey/$testJourneyId")

        res.status mustBe UNAUTHORIZED
      }
    }
    "the provided internal ID does not match the ID on the record" should {
      "return Not Found" in {
        stubAuth(OK, successfulAuthResponse(Some(testIncorrectAuthInternalId)))

        val testData = Json.obj(
          "testField" -> "testValue"
        )
        insertById(testJourneyId, testInternalId, testData)

        val res = get(s"/journey/$testJourneyId")

        res.status mustBe NOT_FOUND
      }
    }
  }


  "GET /journey/:journeyId/:dataKey" when {
    "there is data stored against the journey ID containing the value in dataKey" should {
      "return all the data stored against the journeyId and dataKey" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        val testData = Json.obj(
          testDataKey -> testDataValue
        )
        insertById(testJourneyId, testInternalId, testData)

        val res = get(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe OK
        res.json mustBe JsString(testDataValue)
      }
    }

    "there is data stored against the journey ID but no data for the dataKey" should {
      "return NOT FOUND with a code indicating there is no data" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"
        val newTestDataKey = "newTestDataKey"

        val testData = Json.obj(
          testDataKey -> testDataValue
        )

        insertById(testJourneyId, testInternalId, testData)

        val res = get(s"/journey/$testJourneyId/$newTestDataKey")

        res.status mustBe NOT_FOUND
        res.json mustBe Json.obj(
          "code" -> "NOT_FOUND",
          "reason" -> s"No data exists for either journey ID: $testJourneyId or data key: $newTestDataKey"
        )
      }
    }

    "there is no data stored against the journey ID" should {
      "return all the data stored against the journeyId and dataKey" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        val testDataKey = "testDataKey"

        insertById(testJourneyId, testInternalId)

        val res = get(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe NOT_FOUND
        res.json mustBe Json.obj(
          "code" -> "NOT_FOUND",
          "reason" -> s"No data exists for either journey ID: $testJourneyId or data key: $testDataKey"
        )
      }
    }

    "the user cannot be authorised" should {
      "return Unauthorised" in {
        stubAuthFailure()

        val testDataKey = "testDataKey"

        val res = get(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe UNAUTHORIZED
      }
    }
    "the provided internal ID does not match the ID on the record" should {
      "return Not Found" in {
        stubAuth(OK, successfulAuthResponse(Some(testIncorrectAuthInternalId)))
        val testDataKey = "testDataKey"

        val res = get(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe NOT_FOUND
      }
    }

  }

  "PUT /journey/:journeyId/:dataKey" when {
    "there is a journey for the provided journey ID" should {
      "update the data by adding a new data field" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        insertById(testJourneyId, testInternalId)

        val res = put(s"/journey/$testJourneyId/$testDataKey")(testDataValue)

        res.status mustBe OK

        findById(testJourneyId, testInternalId) mustBe Some(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            testDataKey -> testDataValue
          )
        )
      }
      "update the data by updating an existing field" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        val testData = Json.obj(
          testDataKey -> testDataValue
        )

        insertById(testJourneyId, testInternalId, testData)

        val newTestDataValue = "newTestDataValue"

        val res = put(s"/journey/$testJourneyId/$testDataKey")(newTestDataValue)

        res.status mustBe OK

        findById(testJourneyId, testInternalId) mustBe Some(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            testDataKey -> newTestDataValue
          )
        )
      }
    }
    "there is no journey for the provided journey ID" should {
      "return Internal Server Error" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        val res = put(s"/journey/$testJourneyId/$testDataKey")(testDataValue)

        res.status mustBe INTERNAL_SERVER_ERROR

        findById(testJourneyId, testInternalId) mustBe None
      }
    }
    "the user cannot be authorised" should {
      "return Unauthorised" in {
        stubAuthFailure()

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        val res = put(s"/journey/$testJourneyId/$testDataKey")(testDataValue)

        res.status mustBe UNAUTHORIZED
      }
    }
    "the provided internal ID does not match the ID on the record" should {
      "return Internal Server Error" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        insertById(testJourneyId, testIncorrectAuthInternalId)

        val res = put(s"/journey/$testJourneyId/$testDataKey")(testDataValue)

        res.status mustBe INTERNAL_SERVER_ERROR
      }
    }
  }
  "DELETE /journey/:journeyId/:dataKey" when {
    "there is a journey for the provided journey ID" should {
      "remove the data with the provided data key" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"

        insertById(testJourneyId, testInternalId, Json.obj(testDataKey -> testDataValue))

        val res = delete(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe NO_CONTENT

        findById(testJourneyId, testInternalId) mustBe Some(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId
          )
        )
      }
      "not raise an error if the data key does not exist in the journey data" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

        val testDataKey = "testDataKey"
        val testDataValue = "testDataValue"
        val newTestDataKey = "newTestDataKey"

        insertById(testJourneyId, testInternalId, Json.obj(testDataKey -> testDataValue))

        val res = delete(s"/journey/$testJourneyId/$newTestDataKey")

        res.status mustBe NO_CONTENT

        findById(testJourneyId, testInternalId) mustBe Some(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            testDataKey -> testDataValue
          )
        )
      }
    }
    "there is no journey for the provided journey ID" should {
      "return Internal Server Error" in {
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        val testDataKey = "testDataKey"

        val res = delete(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe INTERNAL_SERVER_ERROR

        findById(testJourneyId, testInternalId) mustBe None
      }
    }
    "the internal ID could not be retrieved from Auth" should {
      "return Unauthorised" in {
        stubAuthFailure()

        val testDataKey = "testDataKey"

        val res = delete(s"/journey/$testJourneyId/$testDataKey")

        res.status mustBe UNAUTHORIZED
      }
    }
  }

}

