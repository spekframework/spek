package org.spekframework.spek2.style.gherkin

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

class FeatureBodyAliases {

  lateinit var groupBody: FakeGroupBody
  lateinit var featureBody: FeatureBody

  @BeforeTest
  fun createFakeGroup() {
    groupBody = FakeGroupBody()
    featureBody = FeatureBody(groupBody)
  }

  @Test
  fun beforeEachScenarioShouldCallBeforeEachGroup() {
    val fixture = { println("hello") }
    featureBody.beforeEachScenario(fixture)
    assertSame(fixture, groupBody.beforeEachGroup)
  }

  @Test
  fun afterEachScenarioShouldCallAfterEachGroup() {
    val fixture = { println("hello") }
    featureBody.afterEachScenario(fixture)
    assertSame(fixture, groupBody.afterEachGroup)
  }

  @Test
  fun beforeFeatureShouldCallBeforeGroup() {
    val fixture = { println("hello") }
    featureBody.beforeFeature(fixture)
    assertSame(fixture, groupBody.beforeGroup)
  }

  @Test
  fun afterFeatureShouldCallAfterGroup() {
    val fixture = { println("hello") }
    featureBody.afterFeature(fixture)
    assertSame(fixture, groupBody.afterGroup)
  }
}
