package org.jetbrains.spek.engine.test.event

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.reporting.ReportEntry

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
