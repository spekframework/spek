package org.spekframework.spek2

import org.spekframework.spek2.style.gherkin.FeatureBody
import kotlin.test.assertSame

object FeatureBodyAliasesTest : Spek({

    val groupBody by memoized { FakeGroupBody() }
    val featureBody by memoized { FeatureBody(groupBody) }

    test("beforeEachScenario should call beforeEachGroup") {
        val fixture = suspend { println("hello") }
        featureBody.beforeEachScenario(fixture)
        assertSame(fixture, groupBody.beforeEachGroup)
    }

    test("afterEachScenario should call AfterEachGroup") {
        val fixture = suspend { println("hello") }
        featureBody.afterEachScenario(fixture)
        assertSame(fixture, groupBody.afterEachGroup)
    }

    test("beforeFeature should call BeforeGroup") {
        val fixture = suspend { println("hello") }
        featureBody.beforeFeature(fixture)
        assertSame(fixture, groupBody.beforeGroup)
    }

    test("afterFeature should call AfterGroup") {
        val fixture = suspend { println("hello") }
        featureBody.afterFeature(fixture)
        assertSame(fixture, groupBody.afterGroup)
    }
})
