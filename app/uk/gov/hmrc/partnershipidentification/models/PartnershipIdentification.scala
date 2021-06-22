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

package uk.gov.hmrc.partnershipidentification.models

import play.api.libs.json._

case class PartnershipIdentification(journeyId: String)

object PartnershipIdentification {

  implicit object MongoFormat extends OFormat[PartnershipIdentification] {
    override def writes(o: PartnershipIdentification): JsObject =
      Json.obj("_id" -> o.journeyId)

    override def reads(json: JsValue): JsResult[PartnershipIdentification] =
      for {
        journeyId <- (json \ "_id").validate[String]
      } yield PartnershipIdentification(journeyId)
  }

}