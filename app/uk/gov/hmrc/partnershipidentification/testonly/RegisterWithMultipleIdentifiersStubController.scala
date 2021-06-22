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

package uk.gov.hmrc.partnershipidentification.testonly

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

@Singleton
class RegisterWithMultipleIdentifiersStubController @Inject()(controllerComponents: ControllerComponents) extends BackendController(controllerComponents) {

  val registerWithMultipleIdentifiers: Action[AnyContent] = Action {
    val stubbedSafeId = "X00000123456789"

    Ok(Json.obj(
      "identification" -> Json.arr(
        Json.obj(
          "idType" -> "SAFEID",
          "idValue" -> stubbedSafeId
        )
      )
    ))
  }
}