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

package uk.gov.hmrc.partnershipidentification.services

import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.partnershipidentification.connectors.PartnershipKnownFactsConnector
import uk.gov.hmrc.partnershipidentification.services.ValidatePartnershipInformationService._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ValidatePartnershipInformationService @Inject()(partnershipKnownFactsConnector: PartnershipKnownFactsConnector)
                                                     (implicit ec: ExecutionContext) {

  def validate(saUtr: String,
               postCode: String
              )(implicit hc: HeaderCarrier): Future[ValidatePartnershipInformationResponse] =
    partnershipKnownFactsConnector.getPartnershipKnownFacts(saUtr).map {
      case knownFacts if knownFacts contains postCode =>
        Right(PostCodeMatched)
      case _ =>
        Left(PostCodeDoesNotMatch)
    }
}

object ValidatePartnershipInformationService {

  type ValidatePartnershipInformationResponse = Either[PostCodeDoesNotMatch.type, PostCodeMatched.type]

  case object PostCodeMatched

  case object PostCodeDoesNotMatch

}
