package org.spekframework.runtime.runner

import org.spekframework.runtime.TrackingExecutionListener
import org.spekframework.runtime.execution.ExecutionResult
import org.spekframework.runtime.scope.ActionScopeImpl
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.scope.TestScopeImpl

const val SCOPE_PREFIX = "+--"
const val SCOPE_INDENT = "|  "

abstract class ConsolePrinter: TrackingExecutionListener() {
    private var indent = 0

    override fun executionFinish() {
        printSummary()
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        super.testExecutionStart(test)
        printPath(test.path)
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        super.testExecutionFinish(test, result)
        printResults(result)
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        super.testIgnored(test, reason)
        printIgnored(test.path, reason)
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        super.groupExecutionStart(group)
        printPath(group.path)
        indent()
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        super.groupExecutionFinish(group, result)
        printResults(result)
        unindent()
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        super.groupIgnored(group, reason)
        printIgnored(group.path, reason)
    }

    override fun actionExecutionStart(action: ActionScopeImpl) {
        super.actionExecutionStart(action)
        printPath(action.path)
        indent()
    }

    override fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult) {
        super.actionExecutionFinish(action, result)
        printResults(result)
        unindent()
    }

    override fun actionIgnored(action: ActionScopeImpl, reason: String?) {
        super.actionIgnored(action, reason)
        printIgnored(action.path, reason)
    }

    protected abstract fun extractStacktrace(throwable: Throwable): String

    private fun indent() {
        indent++
    }

    private fun unindent() {
        indent--
    }

    private fun printIndent() {
        print(SCOPE_INDENT.repeat(indent))
    }
    private fun printPath(path: Path, newline: Boolean = true) {
        printIndent()
        print("$SCOPE_PREFIX${path.name}")

        if (newline) {
            println()
        }
    }

    private fun printIgnored(path: Path, reason: String?) {
        printIndent()
        printPath(path, false)
        println(" IGNORED")
    }

    private fun printResults(result: ExecutionResult) {
        if (result is ExecutionResult.Failure) {
            indent()
            printIndent()
            println()

            printIndent()
            println(result.cause.message)

            printIndent()
            println()

            val stacktrace = extractStacktrace(result.cause).
                prependIndent(SCOPE_INDENT.repeat(indent))

            println(stacktrace)

            unindent()
        }
    }

    private fun printSummary() {
        println()
        println("Results: ")
        println("  $groupsExecuted group(s) found.")
        println("  $groupsIgnored group(s) ignored.")
        println("  $groupsFailed group(s) failed.")

        println("  $actionsExecuted action(s) found.")
        println("  $actionsIgnored action(s) ignored.")
        println("  $actionsFailed action(s) failed.")

        println("  $testsExecuted test(s) found.")
        println("  $testsIgnored test(s) ignored.")
        println("  $testsFailed test(s) failed.")
    }
}
