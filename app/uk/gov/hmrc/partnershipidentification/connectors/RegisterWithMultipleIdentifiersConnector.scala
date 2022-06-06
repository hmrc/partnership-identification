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

package uk.gov.hmrc.partnershipidentification.connectors

import play.api.http.Status.OK
import play.api.libs.json._
import uk.gov.hmrc.http._
import uk.gov.hmrc.partnershipidentification.config.AppConfig
import uk.gov.hmrc.partnershipidentification.connectors.RegisterWithMultipleIdentifiersHttpParser._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RegisterWithMultipleIdentifiersConnector @Inject()(http: HttpClient,
                                                         appConfig: AppConfig
                                                        )(implicit ec: ExecutionContext) {

  def register(jsonBody: JsObject, regime: String
              )(implicit hc: HeaderCarrier): Future[RegisterWithMultipleIdentifiersResult] = {

    val extraHeaders = Seq(
      "Authorization" -> appConfig.desAuthorisationToken,
      appConfig.desEnvironmentHeader,
      "Content-Type" -> "application/json"
    )


    http.POST[JsObject, RegisterWithMultipleIdentifiersResult](
      url = appConfig.getRegisterWithMultipleIdentifiersUrl(regime),
      headers = extraHeaders,
      body = jsonBody
    )(
      implicitly[Writes[JsObject]],
      RegisterWithMultipleIdentifiersHttpReads,
      hc,
      ec
    )

  }

}

object RegisterWithMultipleIdentifiersHttpParser {

  val IdentificationKey = "identification"
  val IdentificationTypeKey = "idType"
  val IdentificationValueKey = "idValue"
  val SafeIdKey = "SAFEID"

  implicit object RegisterWithMultipleIdentifiersHttpReads extends HttpReads[RegisterWithMultipleIdentifiersResult] {

    override def read(method: String, url: String, response: HttpResponse): RegisterWithMultipleIdentifiersResult = {
      response.status match {
        case OK =>
          (for {
            idType <- (response.json \ IdentificationKey \ 0 \ IdentificationTypeKey).validate[String]
            if idType == SafeIdKey
            safeId <- (response.json \ IdentificationKey \ 0 \ IdentificationValueKey).validate[String]
          } yield safeId) match {
            case JsSuccess(safeId, _) => RegisterWithMultipleIdentifiersSuccess(safeId)
            case _: JsError => throw new InternalServerException(s"Invalid JSON returned on Register API: ${response.body}")
          }
        case _ => handleFailureResponse(response)
      }
    }
  }

  private def handleFailureResponse(response: HttpResponse): RegisterWithMultipleIdentifiersFailure = {
    if(response.json.as[JsObject].keys.contains("failures")){
      (response.json \ "failures").validate[Array[Failure]] match {
        case JsSuccess(failures, _) => RegisterWithMultipleIdentifiersFailure(response.status, failures)
        case _: JsError => throw new InternalServerException(s"Invalid JSON returned on Register API: ${response.body}")
      }
    } else {
      response.json.validate[Failure] match {
        case JsSuccess(failure, _) => RegisterWithMultipleIdentifiersFailure(response.status, Array(failure))
        case _: JsError => throw new InternalServerException(s"Invalid JSON returned on Register API: ${response.body}")
      }
    }
  }


  sealed trait RegisterWithMultipleIdentifiersResult

  case class RegisterWithMultipleIdentifiersSuccess(safeId: String) extends RegisterWithMultipleIdentifiersResult

  case class RegisterWithMultipleIdentifiersFailure(status: Int, body: Array[Failure]) extends RegisterWithMultipleIdentifiersResult

  case class Failure(code: String, reason: String)

  implicit val formatFailure: OFormat[Failure] = Json.format[Failure]
}
