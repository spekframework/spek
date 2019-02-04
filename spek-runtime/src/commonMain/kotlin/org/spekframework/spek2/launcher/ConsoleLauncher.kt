package org.spekframework.spek2.launcher

import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.TestScopeImpl

sealed class Reporter
class ConsoleReporter(val format: Format): Reporter() {
    enum class Format {
        BASIC,
        SERVICE_MESSAGE
    }
}

data class LauncherArgs(
    val reporters: List<Reporter>,
    val paths: List<Path>,
    val reportExitCode: Boolean
)

class CompoundExecutionListener(val listeners: List<ExecutionListener>): ExecutionListener {
    private var hasFailure = false

    fun isSuccessful() = !hasFailure

    override fun executionStart() {
        listeners.forEach { it.executionStart() }
    }

    override fun executionFinish() {
        listeners.forEach { it.executionFinish() }
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        listeners.forEach { it.testExecutionStart(test) }
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        listeners.forEach { it.testExecutionFinish(test, result) }
        maybeRecordFailure(result)
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        listeners.forEach { it.testIgnored(test, reason) }
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        listeners.forEach { it.groupExecutionStart(group) }
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        listeners.forEach { it.groupExecutionFinish(group, result) }
        maybeRecordFailure(result)
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        listeners.forEach { it.groupIgnored(group, reason) }
    }

    private fun maybeRecordFailure(result: ExecutionResult) {
        if (result !is ExecutionResult.Success) {
            hasFailure = true
        }
    }

}

abstract class AbstractConsoleLauncher {
    fun launch(args: List<String>): Int {
        val parsedArgs = parseArgs(args)
        val listeners = createListenersFor(parsedArgs.reporters)
        val runtime = SpekRuntime()

        val discoveryRequest = DiscoveryRequest(emptyList(), parsedArgs.paths)
        val discoveryResult = runtime.discover(discoveryRequest)

        val listener = CompoundExecutionListener(listeners)
        val executionRequest = ExecutionRequest(discoveryResult.roots, listener)
        runtime.execute(executionRequest)

        return when {
            parsedArgs.reportExitCode && !listener.isSuccessful() -> -1
            parsedArgs.reportExitCode && listener.isSuccessful() -> 0
            else -> 0
        }
    }

    private fun createListenersFor(reporters: List<Reporter>): List<ExecutionListener> {
        TODO()
    }

    protected abstract fun parseArgs(args: List<String>): LauncherArgs
}

expect class ConsoleLauncher: AbstractConsoleLauncher