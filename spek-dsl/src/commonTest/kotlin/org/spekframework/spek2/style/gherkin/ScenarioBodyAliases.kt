package org.spekframework.spek2.style.gherkin

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

class ScenarioBodyAliases {

  lateinit var groupBody: FakeGroupBody
  lateinit var scenarioBody: ScenarioBody

  @BeforeTest
  fun createFakeGroup() {
    groupBody = FakeGroupBody()
    scenarioBody = ScenarioBody(groupBody)
  }

  @Test
  fun beforeEachScenarioShouldCallBeforeEachGroup() {
    val fixture = { println("hello") }
    scenarioBody.beforeScenario(fixture)
    assertSame(fixture, groupBody.beforeGroup)
  }

  @Test
  fun afterEachScenarioShouldCallAfterEachGroup() {
    val fixture = { println("hello") }
    scenarioBody.afterScenario(fixture)
    assertSame(fixture, groupBody.afterGroup)
  }

  @Test
  fun beforeEachStepShouldCallBeforeEachTest() {
    val fixture = { println("hello") }
    scenarioBody.beforeEachStep(fixture)
    assertSame(fixture, groupBody.beforeEachTest)
  }

  @Test
  fun afterEachStepShouldCallAfterEachTest() {
    val fixture = { println("hello") }
    scenarioBody.afterEachStep(fixture)
    assertSame(fixture, groupBody.afterEachTest)
  }
}
