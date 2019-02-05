package org.spekframework.spek2.launcher

import org.spekframework.spek2.launcher.reporter.BasicConsoleReporter
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.TestScopeImpl

sealed class ReporterType
class ConsoleReporterType(val format: Format): ReporterType() {
    enum class Format {
        BASIC,
        SERVICE_MESSAGE
    }
}

data class LauncherArgs(
    val reporterTypes: List<ReporterType>,
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
    fun launch(context: DiscoveryContext, args: List<String>): Int {
        val parsedArgs = parseArgs(args)
        val listeners = createListenersFor(parsedArgs.reporterTypes)
        val runtime = SpekRuntime()

        val paths = mutableListOf<Path>()
        paths.addAll(parsedArgs.paths)

        // todo: empty paths implies run everything
        if (paths.isEmpty()) {
            paths.add(PathBuilder.ROOT)
        }

        val discoveryRequest = DiscoveryRequest(context, paths)
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

    private fun createListenersFor(reporterTypes: List<ReporterType>): List<ExecutionListener> {
        return reporterTypes.map { reporter ->
            when (reporter) {
                is ConsoleReporterType -> when (reporter.format) {
                    ConsoleReporterType.Format.BASIC -> BasicConsoleReporter()
                    else -> throw AssertionError("Unsupported console reporter: ${reporter.format}")
                }
                else -> throw AssertionError("Unsupported reporter: $reporter")
            }
        }
    }

    private fun parseArgs(args: List<String>): LauncherArgs {
        var rawReporterType: String? = null
        var rawConsoleReporterFormat: String? = null
        var reportExitCode = true

        args.forEach { arg ->
            when {
                arg.startsWith("--reporter=") -> {
                    rawReporterType = arg.split("=")[1]
                }

                arg.startsWith("--console-reporter-format=") -> {
                    rawConsoleReporterFormat = arg.split("=")[1]
                }

                arg == "--dont-report-exit-code" -> reportExitCode = false
                else -> throw AssertionError("Unsupported arg: $arg")
            }
        }

        val reporterType = rawReporterType?.let {
            when (it) {
                "console" -> {
                    val format = if (rawConsoleReporterFormat != null) {
                        when (rawConsoleReporterFormat) {
                            "basic" -> ConsoleReporterType.Format.BASIC
                            "sm" -> ConsoleReporterType.Format.SERVICE_MESSAGE
                            else -> throw AssertionError("Unsupported console reporter format: $rawConsoleReporterFormat")
                        }
                    } else {
                        ConsoleReporterType.Format.BASIC
                    }

                    ConsoleReporterType(format)
                }
                else -> throw AssertionError("Unsupported reporter: $rawReporterType")
            }
        } ?: ConsoleReporterType(ConsoleReporterType.Format.BASIC)

        return LauncherArgs(
            listOf(reporterType),
            emptyList(),
            reportExitCode
        )
    }
}

expect class ConsoleLauncher(): AbstractConsoleLauncher