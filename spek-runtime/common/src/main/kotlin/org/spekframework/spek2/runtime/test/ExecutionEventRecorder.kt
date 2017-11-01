package org.spekframework.spek2.runtime.test

import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.execution.RuntimeExecutionListener
import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl
import org.spekframework.spek2.runtime.test.event.ExecutionEvent

/**
 * @author Ranie Jade Ramiso
 */
class ExecutionEventRecorder: RuntimeExecutionListener() {
    private val _executionEvents = mutableListOf<ExecutionEvent>()
    val executionEvents: List<ExecutionEvent> = _executionEvents
    val testIgnoredCount: Int
        get() = countTestEventsByType<ExecutionEvent.Ignored>()
    val containerIgnoredCount: Int
        get() = countContainerEventsByType<ExecutionEvent.Ignored>()
    val testStartedCount: Int
        get() = countTestEventsByType<ExecutionEvent.Started>()
    val containerStartedCount: Int
        get() = countContainerEventsByType<ExecutionEvent.Started>()
    val testFinishedCount: Int
        get() = countTestEventsByType<ExecutionEvent.Finished>()
    val testSuccessfulCount: Int
        get() = getTestFinishedEventsByStatus<ExecutionResult.Success>().count()
    val testFailureCount: Int
        get() = getTestFinishedEventsByStatus<ExecutionResult.Failure>().count()

    val containerFailureCount: Int
        get()= getContainerFinishedEventsByStatus<ExecutionResult.Failure>().count()

    fun getFailingTestEvents(): Sequence<ExecutionEvent.Finished> {
        return getTestFinishedEventsByStatus<ExecutionResult.Success>()
    }

    override fun executionStart() { }

    override fun executionFinish() { }

    override fun testExecutionStart(test: TestScopeImpl) {
        _executionEvents.add(ExecutionEvent.Started(test))
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        _executionEvents.add(ExecutionEvent.Finished(test, result))
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        _executionEvents.add(ExecutionEvent.Ignored(test, reason))
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        _executionEvents.add(ExecutionEvent.Started(group))
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        _executionEvents.add(ExecutionEvent.Finished(group, result))
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        _executionEvents.add(ExecutionEvent.Ignored(group, reason))
    }

    override fun actionExecutionStart(action: ActionScopeImpl) {
        _executionEvents.add(ExecutionEvent.Started(action))
    }

    override fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult) {
        _executionEvents.add(ExecutionEvent.Finished(action, result))
    }

    override fun actionIgnored(action: ActionScopeImpl, reason: String?) {
        _executionEvents.add(ExecutionEvent.Ignored(action, reason))
    }

    private inline fun <reified T: ExecutionResult> getTestFinishedEventsByStatus(): Sequence<ExecutionEvent.Finished> {
        return getTestEventsByType<ExecutionEvent.Finished>()
            .filter { it.result is T  }
    }

    private inline fun <reified T: ExecutionResult> getContainerFinishedEventsByStatus(): Sequence<ExecutionEvent.Finished> {
        return getContainerEventsByType<ExecutionEvent.Finished>()
            .filter { it.result is T }
    }

    private inline fun <reified T: ExecutionEvent> countTestEventsByType(): Int = getTestEventsByType<T>().count()
    private inline fun <reified T: ExecutionEvent> countContainerEventsByType(): Int = getContainerEventsByType<T>().count()

    private inline fun <reified T: ExecutionEvent> getContainerEventsByType(): Sequence<T> {
        return _executionEvents.asSequence()
            .filter { it.scope is GroupScopeImpl }
            .filterIsInstance<T>()
    }
    private inline fun <reified T: ExecutionEvent> getTestEventsByType(): Sequence<T> {
        return _executionEvents.asSequence()
            .filter { it.scope is TestScopeImpl }
            .filterIsInstance<T>()
    }
}
