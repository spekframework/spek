package com.bushelpowered.slack_integrations

import org.spekframework.spek2.AbstractSpekTest
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.ExecutionResult
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import org.spekframework.spek2.style.specification.describe
import kotlin.test.expect

object LetValueMetaSpec : AbstractSpekTest({ helper ->
    val results by memoized { Results() }

    describe("let values used correctly") {
        it("should have the correct value when invoked in beforeEachTest") {
            helper.executeTest(LetValueInvokedFromBeforeEachTestExample(results))

            val (_, result) = results.afterExecuteTestResults.single()
            expect(ExecutionResult.Success) { result }
        }

        it("should have the correct value when invoked in afterEachTest") {
            helper.executeTest(LetValueInvokedFromAfterEachTestExample(results))

            val (_, result) = results.afterExecuteTestResults.single()
            expect(ExecutionResult.Success) { result }
        }
    }

    describe("let values used incorrectly") {
        it("should fail when invoked in beforeEachGroup") {
            helper.executeTest(LetValueInvokedFromBeforeEachGroupExample(results))

            val (_, result) = results.afterExecuteGroupResults.single()
            result as ExecutionResult.Failure
            expect("letValue() can't be used from beforeEachGroup or afterEachGroup") { result.cause.message }
        }

        it("should fail silently when invoked in afterEachGroup, cuz spek") {
            val events = arrayListOf<String>()
            helper.executeTest(LetValueInvokedFromAfterEachGroupExample(results, events))

            val (_, result) = results.afterExecuteTestResults.single()
            result as ExecutionResult.Success
            expect(listOf(
                    "afterEachGroup called",
                    "letValue() failed: letValue() can't be used from beforeEachGroup or afterEachGroup"
            )) { events }
        }
    }
})

class LetValueInvokedFromBeforeEachGroupExample(private val listener: LifecycleListener) : Spek({
    registerListener(listener)
    val letValue by value { "anything" }
    beforeEachGroup { letValue() }
    test("empty test") {}
})

class LetValueInvokedFromAfterEachGroupExample(
        private val listener: LifecycleListener, val events: MutableList<String>
) : Spek({
    registerListener(listener)
    val letValue by value { "anything" }
    afterEachGroup {
        events.add("afterEachGroup called")
        try {
            letValue()
            events.add("letValue() called")
        } catch (e: Exception) {
            events.add("letValue() failed: ${e.message}")
        }
    }
    test("empty test") {}
})

class LetValueInvokedFromBeforeEachTestExample(private val listener: LifecycleListener) : Spek({
    registerListener(listener)
    val letValue by value { "anything" }
    beforeEachTest {
        expect("anything") { letValue() }
    }
    test("empty test") {}
})

class LetValueInvokedFromAfterEachTestExample(private val listener: LifecycleListener) : Spek({
    registerListener(listener)
    val letValue by value { "anything" }
    afterEachTest {
        expect("anything") { letValue() }
    }
    test("empty test") {}
})

class Results : LifecycleListener {
    val beforeExecuteTestResults = mutableListOf<TestScope>()
    val afterExecuteTestResults = mutableListOf<Pair<TestScope, ExecutionResult>>()
    val beforeExecuteGroupResults = mutableListOf<GroupScope>()
    val afterExecuteGroupResults = mutableListOf<Pair<GroupScope, ExecutionResult>>()

    override fun beforeExecuteTest(test: TestScope) {
        beforeExecuteTestResults.add(test)
    }

    override fun afterExecuteTest(test: TestScope, result: ExecutionResult) {
        afterExecuteTestResults.add(test to result)
    }

    override fun beforeExecuteGroup(group: GroupScope) {
        beforeExecuteGroupResults.add(group)
    }

    override fun afterExecuteGroup(group: GroupScope, result: ExecutionResult) {
        afterExecuteGroupResults.add(group to result)
    }
}