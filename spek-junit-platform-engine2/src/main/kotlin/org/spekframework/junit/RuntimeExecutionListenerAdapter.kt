package org.spekframework.junit

import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.TestExecutionResult
import org.spekframework.runtime.execution.ExecutionContext
import org.spekframework.runtime.execution.ExecutionResult
import org.spekframework.runtime.execution.RuntimeExecutionListener
import org.spekframework.runtime.scope.ActionScopeImpl
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.ScopeImpl
import org.spekframework.runtime.scope.TestScopeImpl

class RuntimeExecutionListenerAdapter(val listener: EngineExecutionListener): RuntimeExecutionListener() {
    fun ScopeImpl.asDescriptor() = TestDescriptorAdapter.asDescriptor(this)
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
