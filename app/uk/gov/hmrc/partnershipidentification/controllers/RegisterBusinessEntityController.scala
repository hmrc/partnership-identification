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
import play.api.mvc.{Action, ControllerComponents, Result}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersHttpParser._
import uk.gov.hmrc.partnershipidentification.services.RegisterWithMultipleIdentifiersService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class RegisterBusinessEntityController @Inject()(cc: ControllerComponents,
                                                 registerWithMultipleIdentifiersService: RegisterWithMultipleIdentifiersService,
                                                 val authConnector: AuthConnector
                                                )(implicit ec: ExecutionContext) extends BackendController(cc) with AuthorisedFunctions {

  def registerGeneralPartnership(): Action[(String, String)] =
    Action.async(parse.json[(String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      regime <- (json \ "regime").validate[String]
    } yield (sautr, regime)
    ))(implicit request => authorised() {
      val (sautr, regime) = request.body

      registerWithMultipleIdentifiersService.registerGeneralPartnership(sautr, regime).map(handleRegisterResponse)
    })

  def registerScottishPartnership(): Action[(String, String)] =
    Action.async(parse.json[(String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      regime <- (json \ "regime").validate[String]
    } yield (sautr, regime)
    ))(implicit request => authorised() {
      val (sautr, regime) = request.body

      registerWithMultipleIdentifiersService.registerScottishPartnership(sautr, regime).map(handleRegisterResponse)
    })

  def registerLimitedPartnership(): Action[(String, String, String)] =
    Action.async(parse.json[(String, String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      companyNumber <- (json \ "companyNumber").validate[String]
      regime <- (json \ "regime").validate[String]
    } yield (sautr, companyNumber, regime)
    ))(implicit request => authorised() {
      val (sautr, companyNumber, regime) = request.body

      registerWithMultipleIdentifiersService.registerLimitedPartnership(sautr, companyNumber, regime).map(handleRegisterResponse)
    })

  def registerLimitedLiabilityPartnership(): Action[(String, String, String)] =
    Action.async(parse.json[(String, String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      companyNumber <- (json \ "companyNumber").validate[String]
      regime <- (json \ "regime").validate[String]
    } yield (sautr, companyNumber, regime)
    ))(implicit request => authorised() {
      val (sautr, companyNumber, regime) = request.body

      registerWithMultipleIdentifiersService.registerLimitedLiabilityPartnership(sautr, companyNumber, regime).map(handleRegisterResponse)
    })

  def registerScottishLimitedPartnership(): Action[(String, String, String)] =
    Action.async(parse.json[(String, String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      companyNumber <- (json \ "companyNumber").validate[String]
      regime <- (json \ "regime").validate[String]
    } yield (sautr, companyNumber, regime)
    ))(implicit request => authorised() {
      val (sautr, companyNumber, regime) = request.body

      registerWithMultipleIdentifiersService.registerScottishLimitedPartnership(sautr, companyNumber, regime).map(handleRegisterResponse)
    })

  private def handleRegisterResponse(registerResult: RegisterWithMultipleIdentifiersResult): Result = registerResult match {
    case RegisterWithMultipleIdentifiersSuccess(safeId) =>
      Ok(Json.obj(
        "registration" -> Json.obj(
          "registrationStatus" -> "REGISTERED",
          "registeredBusinessPartnerId" -> safeId)))
    case _ =>
      Ok(Json.obj(
        "registration" -> Json.obj(
          "registrationStatus" -> "REGISTRATION_FAILED"))
      )
  }
}
