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

package uk.gov.hmrc.partnershipidentification.testonly

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class RegisterWithMultipleIdentifiersStubController @Inject()(controllerComponents: ControllerComponents) extends BackendController(controllerComponents) {

  val singleFailureResultAsString: String =
    s"""{
       |  "code" : "INVALID_PAYLOAD",
       |  "reason" : "Request has not passed validation. Invalid Payload."
       |}""".stripMargin

  val multipleFailureResultAsString: String =
    s"""
       |{
       |    "failures" : [
       |      {
       |        "code" : "INVALID_PAYLOAD",
       |        "reason" : "Request has not passed validation. Invalid Payload."
       |      },
       |      {
       |        "code" : "INVALID_REGIME",
       |        "reason" : "Request has not passed validation. Invalid Regime."
       |      }
       |    ]
       |}""".stripMargin

  private val multipleFailureResponseAsJson: JsObject = Json.parse(multipleFailureResultAsString).as[JsObject]

  def registerWithMultipleIdentifiers: Action[JsValue] = Action.async(parse.json) {
    implicit request =>

      val sautr: String = (request.body \\ "sautr").map(_.as[String]).head

       sautr match {
         case "0000000002" => Future.successful(BadRequest(singleFailureResultAsString))
         case "0000000003" => Future.successful(BadRequest(multipleFailureResponseAsJson))
         case _ =>
           val stubbedSafeId: String = if(e2eTestData.contains(sautr)) e2eTestData(sautr) else "X00000123456789"
           Future.successful(Ok(createSuccessResponse(stubbedSafeId)))
       }

  }

  private def createSuccessResponse(stubbedSafeId: String): JsObject =
    Json.obj(
      "identification" -> Json.arr(
        Json.obj(
          "idType" -> "SAFEID",
          "idValue" -> stubbedSafeId
        )
      )
    )

  lazy private val plasticPackagingTaxData: Map[String, String] = Map(
    "2111234407" -> "XV0000100382302",
    "1908789914" -> "XX0000100382425",
    "2908789918" -> "XM0000100382429",
    "4908789917" -> "XL0000100382428",
    "4111234406" -> "XZ0000100382301",
    "1908789913" -> "XH0000100382424",
    "8908789915" -> "XJ0000100382426",
    "6908789916" -> "XK0000100382427"
  )

  lazy private val pillar2Data: Map[String, String] = Map(
    "1144440208" -> "XL0000100028993",
    "2187647873" -> "XW0000100029017",
    "1113456543" -> "XS0000100029021"
  )

  lazy private val e2eTestData: Map[String, String] = plasticPackagingTaxData ++ pillar2Data
}
