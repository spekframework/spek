package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object ScopeValueAccessTest: AbstractSpekTest({ helper ->
    describe("scope value with CachingMode.TEST") {
        it("should not be accessible in beforeGroup") {
            val recorder = helper.executeTest(testData.scopeValueAccessTest.TestCachedAccessedInBeforeGroup)
            assertTrue(recorder.hasNoSuccessfulTests())
        }

        it("should not be accessible in afterGroup") {
            val recorder = helper.executeTest(testData.scopeValueAccessTest.TestCachedAccessedInAfterGroup)
            assertEquals(1, recorder.failedGroupCount())
        }
    }
})