package org.jetbrains.spek.engine.test

import org.jetbrains.spek.engine.test.event.ExecutionEvent
import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.reporting.ReportEntry
import java.util.LinkedList

/**
 * @author Ranie Jade Ramiso
 */
class ExecutionEventRecorder: EngineExecutionListener {
    private val _executionEvents = LinkedList<ExecutionEvent>()
    val executionEvents: List<ExecutionEvent> = _executionEvents
    val testSkippedCount: Int
        get() = countTestEventsByType<ExecutionEvent.Skipped>()
    val containerSkippedCount: Int
        get() = countContainerEventsByType<ExecutionEvent.Skipped>()
    val testStartedCount: Int
        get() = countTestEventsByType<ExecutionEvent.Started>()
    val containerStartedCount: Int
        get() = countContainerEventsByType<ExecutionEvent.Started>()
    val testFinishedCount: Int
        get() = countTestEventsByType<ExecutionEvent.Finished>()
    val testSuccessfulCount: Int
        get() = getTestFinishedEventsByStatus(TestExecutionResult.Status.SUCCESSFUL).count()
    val testFailureCount: Int
        get() = getTestFinishedEventsByStatus(TestExecutionResult.Status.FAILED).count()

    val containerFailureCount: Int
        get()= getContainerFinishedEventsByStatus(TestExecutionResult.Status.FAILED).count()

    val reportingEntryPublishedCount: Int
        get() = countTestEventsByType<ExecutionEvent.ReportingEntryPublished>()
    val dynamicTestRegisteredCount: Int
        get() = countTestEventsByType<ExecutionEvent.DynamicTestRegistered>()

    fun getFailingTestEvents(): Sequence<ExecutionEvent.Finished> {
        return getTestFinishedEventsByStatus(TestExecutionResult.Status.FAILED)
    }

    override fun executionFinished(testDescriptor: TestDescriptor, testExecutionResult: TestExecutionResult) {
        _executionEvents.add(ExecutionEvent.Finished(testDescriptor, testExecutionResult))
    }

    override fun executionStarted(testDescriptor: TestDescriptor) {
        _executionEvents.add(ExecutionEvent.Started(testDescriptor))
    }

    override fun reportingEntryPublished(testDescriptor: TestDescriptor, entry: ReportEntry) {
        _executionEvents.add(ExecutionEvent.ReportingEntryPublished(testDescriptor, entry))
    }

    override fun executionSkipped(testDescriptor: TestDescriptor, reason: String) {
        _executionEvents.add(ExecutionEvent.Skipped(testDescriptor, reason))
    }

    override fun dynamicTestRegistered(testDescriptor: TestDescriptor) {
        _executionEvents.add(ExecutionEvent.DynamicTestRegistered(testDescriptor))
    }

    private inline fun getTestFinishedEventsByStatus(type: TestExecutionResult.Status): Sequence<ExecutionEvent.Finished> {
        return getTestEventsByType<ExecutionEvent.Finished>()
            .filter { it.result.status == type }
    }

    private inline fun getContainerFinishedEventsByStatus(type: TestExecutionResult.Status): Sequence<ExecutionEvent.Finished> {
        return getContainerEventsByType<ExecutionEvent.Finished>()
            .filter { it.result.status == type }
    }

    private inline fun <reified T: ExecutionEvent> countTestEventsByType(): Int = getTestEventsByType<T>().count()
    private inline fun <reified T: ExecutionEvent> countContainerEventsByType(): Int = getContainerEventsByType<T>().count()

    private inline fun <reified T: ExecutionEvent> getContainerEventsByType(): Sequence<T> {
        return _executionEvents.asSequence()
            .filter { it.testDescriptor.isContainer }
            .filter { it is T }
            .map { it as T }
    }
    private inline fun <reified T: ExecutionEvent> getTestEventsByType(): Sequence<T> {
        return _executionEvents.asSequence()
            .filter { it.testDescriptor.isTest }
            .filter { it is T }
            .map { it as T }
    }
}
