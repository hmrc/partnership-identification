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

import play.api.libs.json.Json
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.partnershipidentification.models.PartnershipInformation
import uk.gov.hmrc.partnershipidentification.services.ValidatePartnershipInformationService
import uk.gov.hmrc.partnershipidentification.services.ValidatePartnershipInformationService.PostCodeMatched
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ValidatePartnershipInformationController @Inject()(validatePartnershipInformationService: ValidatePartnershipInformationService,
                                                         cc: ControllerComponents,
                                                         val authConnector: AuthConnector
                                                        )(implicit ec: ExecutionContext) extends BackendController(cc) with AuthorisedFunctions {

  def validate(): Action[PartnershipInformation] = Action.async(parse.json[PartnershipInformation]) {
    implicit request =>
      authorised() {
        val identifiersMatchKey = "identifiersMatch"

        validatePartnershipInformationService.validate(request.body.sautr, request.body.postcode).map {
          case Right(PostCodeMatched) =>
            Ok(Json.obj(identifiersMatchKey -> true))
          case _ =>
            Ok(Json.obj(identifiersMatchKey -> false))
        }
      }
  }

}
