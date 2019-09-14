package org.spekframework.spek2

import com.nhaarman.mockitokotlin2.*
import org.spekframework.spek2.lifecycle.ExecutionResult
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.style.specification.describe
import java.lang.RuntimeException

object LifecycleListenerTest: AbstractSpekTest({ helper ->
    val listener by memoized { mock<LifecycleListener>() }

    describe("failed test") {
        it("should notify listener when test fails") {
            helper.executeTest(testData.lifecycleListenerTest.TestFailureTest(listener))
            verify(listener).afterExecuteTest(any(), argThat { this is ExecutionResult.Failure && this.cause is RuntimeException})
        }

        it("should notify listener when before each test throws exception") {
            helper.executeTest(testData.lifecycleListenerTest.BeforeEachTestFailureTest(listener))
            verify(listener).afterExecuteTest(any(), argThat { this is ExecutionResult.Failure && this.cause is RuntimeException})
        }

        it("should notify to listener when after each test throws exception") {
            // afterExecuteTest is executed right before after each tests fixtures, so any failures in those
            // fixtures are not reported.
            helper.executeTest(testData.lifecycleListenerTest.AfterEachTestFailureTest(listener))
            verify(listener).afterExecuteTest(any(), argThat { this is ExecutionResult.Failure && this.cause is RuntimeException})
        }
    }

    describe("failed group") {
        it("should notify listener when before group throws exception") {
            helper.executeTest(testData.lifecycleListenerTest.BeforeGroupFailureTest(listener))
            verify(listener).afterExecuteGroup(any(), argThat { this is ExecutionResult.Failure && this.cause is RuntimeException})
        }

        it("should notify to listener when after group throws exception") {
            // afterExecuteGroup is executed right before after group fixtures, so any failures in those
            // fixtures are not reported.
            helper.executeTest(testData.lifecycleListenerTest.AfterGroupFailureTest(listener))
            verify(listener).afterExecuteGroup(any(), argThat { this is ExecutionResult.Failure && this.cause is RuntimeException})
        }
    }
})