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

package uk.gov.hmrc.partnershipidentification.stubs

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.partnershipidentification.utils.WiremockMethods

trait RegisterWithMultipleIdentifiersStub extends WiremockMethods {

  private def registerResponseSuccessBody(safeId: String): JsObject =
    Json.obj(
      "identification" -> Json.arr(
        Json.obj(
          "idType" -> "SAFEID",
          "idValue" -> safeId
        )
      )
    )

  private def registerResponseFailureBody(): JsObject =
    Json.obj(
      "code" -> "INVALID_PAYLOAD",
      "reason" -> "Request has not passed validation. Invalid Payload."
    )

  def stubRegisterGeneralPartnershipWithMultipleIdentifiersSuccess(sautr: String, regime:String)(status: Int, safeId: String): StubMapping = {
    val postBody = Json.obj("ordinaryPartnership" ->
      Json.obj("sautr" -> sautr
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseSuccessBody(safeId)
      )
  }

  def stubRegisterGeneralPartnershipWithMultipleIdentifiersFailure(sautr: String ,regime:String)(status: Int): StubMapping = {
    val postBody = Json.obj("ordinaryPartnership" ->
      Json.obj("sautr" -> sautr
      ))
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseFailureBody()
      )
  }
  def stubRegisterScottishPartnershipWithMultipleIdentifiersSuccess(sautr: String, regime:String)(status: Int, safeId: String): StubMapping = {
    val postBody = Json.obj("scottishPartnership" ->
      Json.obj("sautr" -> sautr
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseSuccessBody(safeId)
      )
  }

  def stubRegisterScottishPartnershipWithMultipleIdentifiersFailure(sautr: String, regime:String)(status: Int): StubMapping = {
    val postBody = Json.obj("scottishPartnership" ->
      Json.obj("sautr" -> sautr
      ))
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseFailureBody()
      )
  }

  def stubRegisterLimitedPartnershipWithMultipleIdentifiersSuccess(sautr: String, companyNumber: String, regime:String)(status: Int, safeId: String): StubMapping = {
    val postBody = Json.obj("limitedPartnership" ->
      Json.obj(
        "sautr" -> sautr,
        "crn" -> companyNumber
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseSuccessBody(safeId)
      )
  }

  def stubRegisterLimitedPartnershipWithMultipleIdentifiersFailure(sautr: String, companyNumber: String, regime:String)(status: Int): StubMapping = {
    val postBody = Json.obj("limitedPartnership" ->
      Json.obj(
        "sautr" -> sautr,
        "crn" -> companyNumber
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseFailureBody()
      )
  }


  def stubRegisterLimitedLiabilityPartnershipWithMultipleIdentifiersSuccess(sautr: String, companyNumber: String, regime:String)(status: Int, safeId: String): StubMapping = {
    val postBody = Json.obj("limitedLiabilityPartnership" ->
      Json.obj(
        "sautr" -> sautr,
        "crn" -> companyNumber
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseSuccessBody(safeId)
      )
  }

  def stubRegisterLimitedLiabilityPartnershipWithMultipleIdentifiersFailure(sautr: String, companyNumber: String, regime:String)(status: Int): StubMapping = {
    val postBody = Json.obj("limitedLiabilityPartnership" ->
      Json.obj(
        "sautr" -> sautr,
        "crn" -> companyNumber
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseFailureBody()
      )
  }


  def stubRegisterScottishLimitedPartnershipWithMultipleIdentifiersSuccess(sautr: String, companyNumber: String, regime:String)(status: Int, safeId: String): StubMapping = {
    val postBody = Json.obj("scottishLimitedPartnership" ->
      Json.obj(
        "sautr" -> sautr,
        "crn" -> companyNumber
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseSuccessBody(safeId)
      )
  }

  def stubRegisterScottishLimitedPartnershipWithMultipleIdentifiersFailure(sautr: String, companyNumber: String, regime:String)(status: Int): StubMapping = {
    val postBody = Json.obj("scottishLimitedPartnership" ->
      Json.obj(
        "sautr" -> sautr,
        "crn" -> companyNumber
      )
    )
    when(method = POST, uri = s"/cross-regime/register/GRS\\?grsRegime=$regime", postBody)
      .thenReturn(
        status = status,
        body = registerResponseFailureBody()
      )
  }

}
