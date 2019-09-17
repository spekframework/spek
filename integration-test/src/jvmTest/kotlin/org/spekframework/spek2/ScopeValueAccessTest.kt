package org.spekframework.spek2

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.style.specification.describe
import testData.scopeValueAccessTest.*
import kotlin.test.assertEquals

object ScopeValueAccessTest: AbstractSpekTest({ helper ->
    describe("scope value with CachingMode.TEST") {
        val mode by memoized { CachingMode.TEST }
        it("should not be accessible in beforeGroup") {
            val recorder = helper.executeTest(CachedAccessedInBeforeGroup(mode))
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should not be accessible in afterGroup") {
            val recorder = helper.executeTest(CachedAccessedInAfterGroup(mode))
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should not be accessible in beforeEachGroup") {
            val recorder = helper.executeTest(CachedAccessedInBeforeEachGroup(mode))
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should not be accessible in afterEachGroup") {
            val recorder = helper.executeTest(CachedAccessedInAfterEachGroup(mode))
            assertEquals(1, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachTest") {
            val recorder = helper.executeTest(CachedAccessedInBeforeEachTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachTest") {
            val recorder = helper.executeTest(CachedAccessedInAfterEachTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible inside a test") {
            val recorder = helper.executeTest(CachedAccessedInTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }
    }

    describe("scope value with CachingMode.SCOPE") {
        val mode by memoized { CachingMode.SCOPE }
        it("should be accessible in beforeGroup") {
            val recorder = helper.executeTest(CachedAccessedInBeforeGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterGroup") {
            val recorder = helper.executeTest(CachedAccessedInAfterGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachGroup") {
            val recorder = helper.executeTest(CachedAccessedInBeforeEachGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachGroup") {
            val recorder = helper.executeTest(CachedAccessedInAfterEachGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachTest") {
            val recorder = helper.executeTest(CachedAccessedInBeforeEachTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachTest") {
            val recorder = helper.executeTest(CachedAccessedInAfterEachTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible inside a test") {
            val recorder = helper.executeTest(CachedAccessedInTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }
    }

    describe("scope value with CachingMode.EACH_GROUP") {
        val mode by memoized { CachingMode.EACH_GROUP }
        it("should be accessible in beforeGroup") {
            val recorder = helper.executeTest(CachedAccessedInBeforeGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterGroup") {
            val recorder = helper.executeTest(CachedAccessedInAfterGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachGroup") {
            val recorder = helper.executeTest(CachedAccessedInBeforeEachGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachGroup") {
            val recorder = helper.executeTest(CachedAccessedInAfterEachGroup(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in beforeEachTest") {
            val recorder = helper.executeTest(CachedAccessedInBeforeEachTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible in afterEachTest") {
            val recorder = helper.executeTest(CachedAccessedInAfterEachTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }

        it("should be accessible inside a test") {
            val recorder = helper.executeTest(CachedAccessedInTest(mode))
            assertEquals(0, recorder.failedGroupCount())
        }
    }
})