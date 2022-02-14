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

import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class RegisterWithMultipleIdentifiersStubController @Inject()(controllerComponents: ControllerComponents) extends BackendController(controllerComponents) {

  def registerWithMultipleIdentifiers: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      val sautr: String = (request.body \\ "sautr").map(_.as[String]).head

      val stubbedSafeId = sautr match {
        case "2111234407" => "XV0000100382302" // PPT Testing
        case "1908789914" => "XX0000100382425" // PPT Testing
        case "2908789918" => "XM0000100382429" // PPT Testing
        case "4908789917" => "XL0000100382428" // PPT Testing
        case _ => "X00000123456789"
      }

      Future.successful(Ok(Json.obj(
        "identification" -> Json.arr(
          Json.obj(
            "idType" -> "SAFEID",
            "idValue" -> stubbedSafeId
          )
        ))))
  }
}
