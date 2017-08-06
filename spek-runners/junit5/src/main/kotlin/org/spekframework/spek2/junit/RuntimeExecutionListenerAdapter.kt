package org.spekframework.spek2.junit

import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.TestExecutionResult
import org.spekframework.spek2.runtime.execution.ExecutionContext
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.execution.RuntimeExecutionListener
import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

class RuntimeExecutionListenerAdapter(val listener: EngineExecutionListener,
                                      val factory: TestDescriptorAdapterFactory): RuntimeExecutionListener() {
    fun ScopeImpl.asDescriptor() = factory.create(this)
    fun ExecutionResult.toJunitResult() = when (this) {
        is ExecutionResult.Success -> TestExecutionResult.successful()
        is ExecutionResult.Failure -> TestExecutionResult.failed(this.cause)
    }

    override fun executionStart() { }

    override fun executionFinish() { }

    override fun testExecutionStart(test: TestScopeImpl) {
        listener.executionStarted(test.asDescriptor())
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        listener.executionFinished(test.asDescriptor(), result.toJunitResult())
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        listener.executionSkipped(test.asDescriptor(), reason ?: "<no reason provided>")
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        listener.executionStarted(group.asDescriptor())
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        listener.executionFinished(group.asDescriptor(), result.toJunitResult())
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        listener.executionSkipped(group.asDescriptor(), reason ?: "<no reason provided>")
    }

    override fun actionExecutionStart(action: ActionScopeImpl) {
        listener.executionStarted(action.asDescriptor())
    }

    override fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult) {
        listener.executionFinished(action.asDescriptor(), result.toJunitResult())
    }

    override fun actionIgnored(action: ActionScopeImpl, reason: String?) {
        listener.executionSkipped(action.asDescriptor(), reason ?: "<no reason provided>")
    }

    override fun dynamicTestRegistered(test: TestScopeImpl, context: ExecutionContext) {
        listener.dynamicTestRegistered(test.asDescriptor())
        super.dynamicTestRegistered(test, context)
    }
}
