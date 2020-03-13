package org.spekframework.spek2


import org.spekframework.spek2.style.gherkin.ScenarioBody
import kotlin.test.assertSame

object ScenarioBodyAliasesTest : Spek({

    val groupBody by memoized { FakeGroupBody() }
    val scenarioBody by memoized { ScenarioBody(groupBody) }

    test("beforeEachScenario should call BeforeEachGroup") {
        val fixture = suspend { println("hello") }
        scenarioBody.beforeScenario(fixture)
        assertSame(fixture, groupBody.beforeGroup)
    }

    test("afterEachScenario should call AfterEachGroup") {
        val fixture = suspend { println("hello") }
        scenarioBody.afterScenario(fixture)
        assertSame(fixture, groupBody.afterGroup)
    }

    test("beforeEachStep should call BeforeEachTest") {
        val fixture = suspend { println("hello") }
        scenarioBody.beforeEachStep(fixture)
        assertSame(fixture, groupBody.beforeEachTest)
    }

    test("afterEachStep should call AfterEachTest") {
        val fixture = suspend { println("hello") }
        scenarioBody.afterEachStep(fixture)
        assertSame(fixture, groupBody.afterEachTest)
    }
})
