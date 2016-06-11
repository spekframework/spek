package org.jetbrains.spek.engine.test.event

import org.junit.gen5.engine.TestDescriptor
import org.junit.gen5.engine.TestExecutionResult
import org.junit.gen5.engine.reporting.ReportEntry

/**
 * @author Ranie Jade Ramiso
 */
sealed class ExecutionEvent(val testDescriptor: TestDescriptor) {
    class DynamicTestRegistered(testDescriptor: TestDescriptor): ExecutionEvent(testDescriptor)
    class Skipped(testDescriptor: TestDescriptor, val reason: String? = null): ExecutionEvent(testDescriptor)
    class Started(testDescriptor: TestDescriptor): ExecutionEvent(testDescriptor)
    class Finished(testDescriptor: TestDescriptor, val result: TestExecutionResult): ExecutionEvent(testDescriptor)
    class ReportingEntryPublished(testDescriptor: TestDescriptor, val entry: ReportEntry): ExecutionEvent(testDescriptor)
}
