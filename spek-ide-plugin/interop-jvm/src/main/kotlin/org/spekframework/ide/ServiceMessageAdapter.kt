package org.spekframework.ide

import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import java.io.CharArrayWriter
import java.io.PrintWriter

class ServiceMessageAdapter: ExecutionListener {
    private val durations = mutableMapOf<Path, Long>()
    override fun executionStart() { }

    override fun executionFinish() {
        durations.clear()
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        durations[test.path] = System.currentTimeMillis()
        out("testStarted name='${test.path.name.toServiceMessageSafeString()}'")
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        val name = test.path.name.toServiceMessageSafeString()
        val duration = System.currentTimeMillis() - durations[test.path]!!
        if (result is ExecutionResult.Failure) {
            val exceptionDetails = getExceptionDetails(result)
            out("testFailed name='$name' duration='$duration' message='${exceptionDetails.first}' details='${exceptionDetails.second}'")
        }
        out("testFinished name='$name' duration='$duration'")
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        val name = test.path.name.toServiceMessageSafeString()
        out("testIgnored name='$name' ignoreComment='${reason ?: "no reason provided"}'")
        out("testFinished name='$name'")
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        out("testSuiteStarted name='${group.path.name.toServiceMessageSafeString()}'")
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        val name = group.path.name.toServiceMessageSafeString()
        if (result is ExecutionResult.Failure) {
            val exceptionDetails = getExceptionDetails(result)

            // fake a child test
            out("testStarted name='$name'")
            out("testFailed name='$name' message='${exceptionDetails.first}' details='${exceptionDetails.second}'")
            out("testFinished name='$name'")
        }
        out("testSuiteFinished name='$name'")
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        val name = group.path.name.toServiceMessageSafeString()
        out("testIgnored name='$name' ignoreComment='${reason ?: "no reason provided"}'")
        out("testFinished name='$name'")
    }

    override fun actionExecutionStart(action: ActionScopeImpl) {
        out("testSuiteStarted name='${action.path.name.toServiceMessageSafeString()}'")
    }

    override fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult) {
        val name = action.path.name.toServiceMessageSafeString()
        if (result is ExecutionResult.Failure) {
            val exceptionDetails = getExceptionDetails(result)

            // fake a child test
            out("testStarted name='$name'")
            out("testFailed name='$name' message='${exceptionDetails.first}' details='${exceptionDetails.second}'")
            out("testFinished name='$name'")
        }
        out("testSuiteFinished name='$name'")
    }

    override fun actionIgnored(action: ActionScopeImpl, reason: String?) {
        val name = action.path.name.toServiceMessageSafeString()
        out("testIgnored name='$name' ignoreComment='${reason ?: "no reason provided"}'")
        out("testFinished name='$name'")
    }

    private fun getExceptionDetails(result: ExecutionResult.Failure): Pair<String?, String> {
        val throwable = result.cause
        val writer = CharArrayWriter()
        throwable.printStackTrace(PrintWriter(writer))

        val details = CharArrayWriter().let {
            throwable.printStackTrace(PrintWriter(it))
            it.toString().toServiceMessageSafeString()
        }

        val message = throwable.message?.toServiceMessageSafeString()

        return message to details
    }

    private fun String.toServiceMessageSafeString(): String {
        return this.replace("|", "||")
            .replace("\n", "|n")
            .replace("\r", "|r")
            .replace("'", "|'")
            .replace("[", "|[")
            .replace("]", "|]")
            .replace(Regex("""\\u(\d\d\d\d)""")) {
                "|0x${it.groupValues[1]}"
            }
    }

    private fun out(event: String) {
        /* ensure service message has it's own line*/
        println()
        println("##teamcity[$event]")
    }
}
