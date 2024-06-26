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

package uk.gov.hmrc.partnershipidentification.models

import uk.gov.hmrc.partnershipidentification.models.PartnershipKnownFacts._

case class PartnershipKnownFacts(postCode: Option[String],
                                 correspondencePostCode: Option[String],
                                 basePostCode: Option[String],
                                 commsPostCode: Option[String],
                                 traderPostCode: Option[String]) extends Iterable[String] {

  override def iterator: Iterator[String] = Iterator(postCode, correspondencePostCode, basePostCode, commsPostCode, traderPostCode).flatten

  def contains(postCode: String): Boolean = iterator.map(sanitisePostCode).contains(sanitisePostCode(postCode))

}

object PartnershipKnownFacts {

  private def sanitisePostCode(postCode: String): String = postCode.filterNot(_.isWhitespace).toUpperCase

}
