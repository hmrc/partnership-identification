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

  def registerGeneralPartnership(): Action[String] =
    Action.async(parse.json[String](
      json => (json \ "sautr").validate[String]
    ))(implicit request => authorised() {
      val sautr = request.body

      registerWithMultipleIdentifiersService.registerGeneralPartnership(sautr).map(handleRegisterResponse)
    })

  def registerScottishPartnership(): Action[String] =
    Action.async(parse.json[String](
      json => (json \ "sautr").validate[String]
    ))(implicit request => authorised() {
      val sautr = request.body

      registerWithMultipleIdentifiersService.registerScottishPartnership(sautr).map(handleRegisterResponse)
    })

  def registerLimitedPartnership(): Action[(String, String)] =
    Action.async(parse.json[(String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      companyNumber <- (json \ "companyNumber").validate[String]
    } yield (sautr, companyNumber)
    ))(implicit request => authorised() {
      val (sautr, companyNumber) = request.body

      registerWithMultipleIdentifiersService.registerLimitedPartnership(sautr, companyNumber).map(handleRegisterResponse)
    })

  def registerLimitedLiabilityPartnership(): Action[(String, String)] =
    Action.async(parse.json[(String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      companyNumber <- (json \ "companyNumber").validate[String]
    } yield (sautr, companyNumber)
    ))(implicit request => authorised() {
      val (sautr, companyNumber) = request.body

      registerWithMultipleIdentifiersService.registerLimitedLiabilityPartnership(sautr, companyNumber).map(handleRegisterResponse)
    })

  def registerScottishLimitedPartnership(): Action[(String, String)] =
    Action.async(parse.json[(String, String)](json => for {
      sautr <- (json \ "sautr").validate[String]
      companyNumber <- (json \ "companyNumber").validate[String]
    } yield (sautr, companyNumber)
    ))(implicit request => authorised() {
      val (sautr, companyNumber) = request.body

      registerWithMultipleIdentifiersService.registerScottishLimitedPartnership(sautr, companyNumber).map(handleRegisterResponse)
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
