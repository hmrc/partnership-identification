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

package featureswitch.core.config

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.partnershipidentification.featureswitch.core.config._

class FeatureSwitchRegistrySpec extends AnyWordSpec with Matchers {

  object TestFeatureSwitchRegistry extends FeatureSwitchingModule()

  "FeatureSwitchRegistry" should {

    "contain the feature switches for the Known facts and Register with identifiers stubs" in {

      TestFeatureSwitchRegistry.switches mustBe Seq(StubPartnershipKnownFacts, StubRegisterWithIdentifiers)

    }

    "be able to access the Known facts stub feature switch" in {

      TestFeatureSwitchRegistry("feature-switch.partnership-known-facts-stub") mustBe StubPartnershipKnownFacts

    }

    "be able to access the Register With Identifiers stub feature switch" in {

      TestFeatureSwitchRegistry("feature-switch.register-with-identifiers-stub") mustBe StubRegisterWithIdentifiers

    }

    "raise an IllegalArgumentException when asked to access an unknown feature switch" in {

      try {
        TestFeatureSwitchRegistry("unknown")
        fail("Invalid feature switch")
      } catch {
        case _: IllegalArgumentException => succeed
      }

    }

  }

}
