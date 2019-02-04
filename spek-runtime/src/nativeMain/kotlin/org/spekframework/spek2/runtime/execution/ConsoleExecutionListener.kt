package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class ConsoleExecutionListener : ExecutionListener {
    private var totalTests = 0
    private var passedTests = 0
    private var failedTests = 0
    private var ignoredTests = 0

    private var failedGroups = 0
    private var ignoredGroups = 0

    override fun executionStart() {}

    override fun executionFinish() {
        println()
        println("Test run complete:")
        println("  $totalTests tests, $passedTests passed, $failedTests failed, and $ignoredTests skipped.")
        println("  ${pluralize(failedGroups, "group")} failed to start, and ${pluralize(ignoredGroups, "was", "were")} skipped.")
    }

    private fun pluralize(count: Int, singular: String, plural: String = singular + "s"): String =
            if (count == 1) {
                "$count $singular"
            } else {
                "$count $plural"
            }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        println("${indentFor(group.path)}> ${group.path.name}")
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        print("${indentFor(test.path)}- ${test.path.name}")
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) =
            when (result) {
                is ExecutionResult.Success -> {
                    totalTests++
                    passedTests++

                    println(": passed")
                }
                is ExecutionResult.Failure -> {
                    totalTests++
                    failedTests++

                    println(": failed: ${result.cause}")
                }
            }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        totalTests++
        ignoredTests++

        println("${indentFor(test.path)}- ${test.path.name}: skipped: ${reason ?: "<no reason given>"}")
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) = when (result) {
        is ExecutionResult.Success -> {
        }
        is ExecutionResult.Failure -> {
            failedGroups++
            println("${indentFor(group.path, 1)}! group failed: ${result.cause}")
        }
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        ignoredGroups++

        println("${indentFor(group.path)}- ${group.path.name}: skipped: ${reason ?: "<no reason given>"}")
    }

    val wasSuccessful: Boolean
        get() = failedTests == 0 && failedGroups == 0

    private fun indentFor(path: Path, extraIndent: Int = 0): String = "  ".repeat(path.depth + extraIndent)

    private val Path.depth: Int
        get() = when {
            parent == null || parent == PathBuilder.ROOT -> 0
            else -> 1 + parent.depth
        }
}
