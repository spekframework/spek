package org.jetbrains.spek.api

import org.jetbrains.spek.console.ActionStatusReporter
import org.jetbrains.spek.console.WorkflowReporter
import org.jetbrains.spek.console.executeSpek
import org.jetbrains.spek.junit.JUnitClassRunner
import org.junit.Assert
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.RunNotifier

/**
 * Created by jakub on 29/01/16.
 */
object SpekTestCaseRunnerProvider {
    @JvmStatic fun provideTestCaseRunners() = arrayOf<Any>(ConsoleSpekTestCaseRunner(), JUnitSpekTestCaseRunner())
}

public abstract class TestSpek : Spek(), TestSpekAction {
    override fun description(): String = "42"
    override fun run(action: () -> Unit) {
    }
}

public abstract class SpekTestCaseRunner {
    fun runTest(specExpr: Specification.() -> Unit, vararg expected: String) {
        val actualLog = arrayListOf<String>()
        val spec = object : TestSpek() {}
        spec.specExpr()
        runSpec(spec, actualLog)
        if (expected.size == 0) return
        val actualDump = actualLog.map { it + "\n" }.fold("") { r, i -> r + i }
        val expectedLog = expected
                .flatMap {
                    it
                            .trim()
                            .split("[\r\n]+".toRegex())
                            .map { it.trim() }
                            .filter { it.length > 0 }
                }.filter { it.length > 0 }.toList()

        Assert.assertEquals(
                actualDump,
                expectedLog,
                actualLog
        )
    }

    protected abstract fun <T> runSpec(spec: T, log: MutableList<String>)
            where  T : Spek, T : TestSpekAction
}

class ConsoleSpekTestCaseRunner : SpekTestCaseRunner() {

    override fun <T> runSpec(spec: T, log: MutableList<String>)
            where T : Spek, T : TestSpekAction {
        executeSpek(spec, ConsoleSpekTestCaseRunner.TestLogger(log))
    }

    public class TestLogger(val output: MutableList<String>) : WorkflowReporter {
        private fun step(prefix: String): ActionStatusReporter = object : ActionStatusReporter {
            override fun started() {
                output.add(prefix + " START")
            }

            override fun completed() {
                output.add(prefix + " FINISH")
            }

            override fun skipped(why: String) {
                output.add(prefix + " SKIP")
            }

            override fun pending(why: String) {
                output.add(prefix + " PEND")
            }

            override fun failed(error: Throwable) {
                output.add(prefix + " FAIL:" + error.message)
            }
        }

        override fun spek(spek: String): ActionStatusReporter = step("$spek")
        override fun given(spek: String, given: String): ActionStatusReporter = step("$given")
        override fun on(spek: String, given: String, on: String) = step("$on")
        override fun it(spek: String, given: String, on: String, it: String) = step("$it")
    }
}

class JUnitSpekTestCaseRunner : SpekTestCaseRunner() {

    override fun <T> runSpec(spec: T, log: MutableList<String>)
            where T : Spek, T : TestSpekAction {
        val jUnitRunner = JUnitClassRunner(spec.javaClass, spec)
        val notifier = RunNotifier()
        notifier.addFirstListener(TestJUnitRunListener(log))
        // todo js build ordered hash map
        // todo js call order counts!!
        jUnitRunner.run(notifier)
    }

    public class TestJUnitRunListener(val output: MutableList<String>) : RunListener() {
        override fun testStarted(description: Description?) {
            output.add("${description?.displayName} START")
        }

        override fun testAssumptionFailure(failure: Failure?) {
            output.add("${failure?.description?.displayName} SKIP")
        }

        override fun testFinished(description: Description?) {
            output.add("${description?.displayName} FINISH")
        }

        override fun testFailure(failure: Failure?) {
            output.add("${failure?.description?.displayName} FAIL: ${failure?.exception?.message}")
        }

        override fun testIgnored(description: Description?) {
            output.add("${description?.displayName} PEND")
        }
    }
}