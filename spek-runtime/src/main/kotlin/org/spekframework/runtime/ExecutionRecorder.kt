package org.spekframework.runtime

import org.spekframework.runtime.execution.ExecutionListener
import org.spekframework.runtime.execution.ExecutionResult
import org.spekframework.runtime.execution.RuntimeExecutionListener
import org.spekframework.runtime.scope.ActionScopeImpl
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.TestScopeImpl

/**
 * TODO: compute stats
 */
class ExecutionRecorder(
    val listeners: List<ExecutionListener>
): RuntimeExecutionListener() {
    override fun executionStart() {
        listeners.forEach { it.executionStart() }
    }

    override fun executionFinish() {
        listeners.forEach { it.executionFinish() }
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        listeners.forEach { it.testExecutionStart(test) }
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        listeners.forEach { it.testExecutionFinish(test, result) }
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        listeners.forEach { it.testIgnored(test, reason) }
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        listeners.forEach { it.groupExecutionStart(group) }
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        listeners.forEach { it.groupExecutionFinish(group, result) }
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        listeners.forEach { it.groupIgnored(group, reason) }
    }

    override fun actionExecutionStart(action: ActionScopeImpl) {
        listeners.forEach { it.actionExecutionStart(action) }
    }

    override fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult) {
        listeners.forEach { it.actionExecutionFinish(action, result) }
    }

    override fun actionIgnored(action: ActionScopeImpl, reason: String?) {
        listeners.forEach { it.actionIgnored(action, reason) }
    }
}
