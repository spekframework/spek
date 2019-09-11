package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe
import testData.scopeValueAccessTest.scope.*
import testData.scopeValueAccessTest.test.*
import kotlin.test.assertEquals

object ScopeValueAccessTest: AbstractSpekTest({ helper ->
    describe("scope value with CachingMode.TEST") {
        it("should not be accessible in beforeGroup") {
            val recorder = helper.executeTest(TestCachedAccessedInBeforeGroup)
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should not be accessible in afterGroup") {
            val recorder = helper.executeTest(TestCachedAccessedInAfterGroup)
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should not be accessible in beforeEachGroup") {
            val recorder = helper.executeTest(TestCachedAccessedInBeforeEachGroup)
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should not be accessible in afterEachGroup") {
            val recorder = helper.executeTest(TestCachedAccessedInAfterEachGroup)
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachTest") {
            val recorder = helper.executeTest(TestCachedAccessedInBeforeEachTest)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachTest") {
            val recorder = helper.executeTest(TestCachedAccessedInAfterEachTest)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible inside a test") {
            val recorder = helper.executeTest(TestCachedAccessedInTest)
            assertEquals(0, recorder.failedGroupCount())
        }
    }

    describe("scope value with CachingMode.SCOPE") {
        it("should be accessible in beforeGroup") {
            val recorder = helper.executeTest(ScopeCachedAccessedInBeforeGroup)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterGroup") {
            val recorder = helper.executeTest(ScopeCachedAccessedInAfterGroup)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachGroup") {
            val recorder = helper.executeTest(ScopeCachedAccessedInBeforeEachGroup)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachGroup") {
            val recorder = helper.executeTest(ScopeCachedAccessedInAfterEachGroup)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachTest") {
            val recorder = helper.executeTest(ScopeCachedAccessedInBeforeEachTest)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachTest") {
            val recorder = helper.executeTest(ScopeCachedAccessedInAfterEachTest)
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible inside a test") {
            val recorder = helper.executeTest(ScopeCachedAccessedInTest)
            assertEquals(0, recorder.failedGroupCount())
        }
    }
})