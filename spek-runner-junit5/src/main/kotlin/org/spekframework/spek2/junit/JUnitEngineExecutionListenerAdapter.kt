package org.spekframework.spek2.junit

import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.TestExecutionResult
import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class JUnitEngineExecutionListenerAdapter(
    private val listener: EngineExecutionListener,
    private val factory: SpekTestDescriptorFactory
) : ExecutionListener {

    companion object {
        private const val DEFAULT_IGNORE_REASON = "<no reason provided>"
    }

    override fun executionStart() = Unit

    override fun executionFinish() = Unit

    override fun testExecutionStart(test: TestScopeImpl) {
        listener.executionStarted(test.asJUnitDescriptor())
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        listener.executionFinished(test.asJUnitDescriptor(), result.asJUnitResult())
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        listener.executionSkipped(test.asJUnitDescriptor(), reason ?: DEFAULT_IGNORE_REASON)
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        listener.executionStarted(group.asJUnitDescriptor())
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        listener.executionFinished(group.asJUnitDescriptor(), result.asJUnitResult())
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        listener.executionSkipped(group.asJUnitDescriptor(), reason ?: DEFAULT_IGNORE_REASON)
    }

    private fun ScopeImpl.asJUnitDescriptor() = factory.create(this)

    private fun ExecutionResult.asJUnitResult() = when (this) {
        is ExecutionResult.Success -> TestExecutionResult.successful()
        is ExecutionResult.Failure -> TestExecutionResult.failed(this.cause)
    }
}
