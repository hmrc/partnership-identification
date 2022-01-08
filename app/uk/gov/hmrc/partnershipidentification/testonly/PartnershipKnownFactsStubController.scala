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

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, InjectedController}

import javax.inject.Singleton
import scala.concurrent.Future

@Singleton
class PartnershipKnownFactsStubController extends InjectedController {

  def getPartnershipKnownFacts(sautr: String): Action[AnyContent] = Action.async {
    Future.successful(sautr match {
      case "0000000000" => NotFound
      case "0000000001" => Ok(Json.obj())
      case _ => Ok(Json.obj(
        "returnType" -> "P",
        "postCode" -> "TF34ER",
        "txpName" -> "Joe Bloggs",
        "address4" -> "Test street",
        "correspondenceDetails" -> Json.obj(
          "corresEveningTelNum" -> "0123456789",
          "corresMobileNum" -> "0123456789",
          "corresFaxNum" -> "0123456789",
          "corresEmailAdd" -> "joe@bloggs.com",
          "correspondencePostCode" -> "AA11AA"
        ),
        "actingInCapacityDetails" -> Json.obj(
          "aicTaxpayerName" -> "Joe Bloggs",
          "capTelephoneNum" -> "0123456789",
          "capEveningTelNum" -> "0123456789",
          "capMobileNum" -> "0123456789",
          "capFaxNum" -> "0123456789",
          "capEmailAdd" -> "joe@bloggs.com"
        ),
        "baseTaxpayerName" -> "Joe Bloggs",
        "taxpayerContactDetails" -> Json.obj(
          "txpTelephoneNum" -> "0123456789",
          "txpEveningTelNum" -> "0123456789",
          "txpMobileNum" -> "0123456789",
          "txpFaxNum" -> "0123456789",
          "txpEmailAdd" -> "joe@bloggs.com"
        ),
        "basePostCode" -> "TF3 4ER",
        "commsPostCode" -> "TF3 4ER",
        "commsTelephoneNum" -> "0123456789",
        "traderName" -> "Foo Bar",
        "traderPostCode" -> "TF3 4ER",
        "dateOfBirth" -> "2012-12-12"
      ))
    })
  }

}
