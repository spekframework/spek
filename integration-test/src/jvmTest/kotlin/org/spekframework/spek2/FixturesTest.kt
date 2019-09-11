package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object FixturesTest: AbstractSpekTest({ helper ->
    describe("before/after each group fixture") {
        it("should be executed for each group") {
            val recorder = helper.executeTest(testData.fixturesTest.EachGroupFixtureTest)
            assertEquals(0, recorder.failedTestCount())
        }
    }
})