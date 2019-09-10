package org.spekframework.spek2

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.discovery.DiscoveryContextBuilder
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import kotlin.test.assertEquals


sealed class ExecutionEvent {
    data class Test(val description: String, val success: Boolean): ExecutionEvent()
    data class TestIgnored(val description: String, val reason: String?): ExecutionEvent()
    class GroupStart(val description: String): ExecutionEvent()
    class GroupEnd(val description: String, val success: Boolean): ExecutionEvent()
    data class GroupIgnored(val description: String, val reason: String?): ExecutionEvent()
}

class ExecutionTreeBuilder {
    private val events = mutableListOf<ExecutionEvent>()

    fun groupStart(description: String) {
        events.add(ExecutionEvent.GroupStart(description))
    }

    fun groupEnd(description: String, success: Boolean = true) {
        events.add(ExecutionEvent.GroupEnd(description, success))
    }

    fun groupIgnored(description: String, reason: String?) {
        events.add(ExecutionEvent.GroupIgnored(description, reason))
    }

    fun test(description: String, success: Boolean = true) {
        events.add(ExecutionEvent.Test(description, success))
    }

    fun testIgnored(description: String, reason: String?) {
        events.add(ExecutionEvent.TestIgnored(description, reason))
    }

    fun group(description: String, success: Boolean = true, block: ExecutionTreeBuilder.() -> Unit) {
        groupStart(description)
        this.block()
        groupEnd(description, success)
    }

    fun build() = events.toList()
}

class ExecutionRecorder: ExecutionListener {
    private var started = false
    private var finished = false
    private lateinit var treeBuilder: ExecutionTreeBuilder

    override fun executionStart() {
        started = true
        treeBuilder = ExecutionTreeBuilder()
    }

    override fun executionFinish() {
        finished = false
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        // nada
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        treeBuilder.test(test.path.name, result is ExecutionResult.Success)
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        treeBuilder.testIgnored(test.path.name, reason)
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        treeBuilder.groupStart(group.path.name)
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        treeBuilder.groupEnd(group.path.name, result is ExecutionResult.Success)
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        treeBuilder.groupIgnored(group.path.name, reason)
    }

    fun events(): List<ExecutionEvent> {
        return treeBuilder.build()
    }
}

class SpekTestHelper {
    fun discoveryContext(block: DiscoveryContextBuilder.() -> Unit): DiscoveryContext {
        val builder = DiscoveryContextBuilder()
        builder.block()
        return builder.build()
    }

    fun executeTests(context: DiscoveryContext, vararg paths: Path): ExecutionRecorder {
        val runtime = SpekRuntime()
        val recorder = ExecutionRecorder()

        val discoveryResult = runtime.discover(DiscoveryRequest(context, listOf(*paths)))
        runtime.execute(ExecutionRequest(discoveryResult.roots, recorder))

        return recorder
    }

    fun<T: Spek> executeTest(test: T): ExecutionRecorder {
        val path = PathBuilder.from(test::class)
            .build()

        return executeTests(discoveryContext {
            addTest(test::class) { test }
        }, path)
    }

    fun assertExecutionEquals(actual: List<ExecutionEvent>, block: ExecutionTreeBuilder.() -> Unit) {
        val builder = ExecutionTreeBuilder()
        builder.block()
        val expected = builder.build()

        assertEquals(executionToString(actual), executionToString(expected))
    }

    /**
     * Prints out something like the following
     *
     * foo bar {
     *   should do this -> PASSED
     *   should fail -> FAILED
     *   should be ignored -> IGNORED
     * }
     * ignored -> IGNORED
     */
    private fun executionToString(events: List<ExecutionEvent>): String {
        var nest = 0
        val builder = StringBuilder()

        val indent = {
            builder.append("  ".repeat(nest))
        }

        events.forEach { event ->
            when (event) {
                is ExecutionEvent.GroupStart -> {
                    indent()
                    builder.append("${event.description} {\n")
                    nest += 1
                }
                is ExecutionEvent.GroupEnd -> {
                    nest -= 1
                    indent()
                    builder.append("}\n")
                }
                is ExecutionEvent.GroupIgnored -> {
                    indent()
                    builder.append("${event.description} -> IGNORED(${event.reason})\n")
                }
                is ExecutionEvent.Test -> {
                    indent()
                    val result = if (event.success) {
                        "PASSED"
                    } else {
                        "FAILED"
                    }
                    builder.append("${event.description} -> $result\n")
                }
                is ExecutionEvent.TestIgnored -> {
                    indent()
                    builder.append("${event.description} -> IGNORED(${event.reason})\n")
                }
            }
        }

        return builder.toString()
    }
}

abstract class AbstractSpekTest(block: Root.(SpekTestHelper) -> Unit): Spek({
    block(SpekTestHelper())
})
