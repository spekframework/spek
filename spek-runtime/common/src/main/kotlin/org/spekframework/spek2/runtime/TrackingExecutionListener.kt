package org.spekframework.spek2.runtime

import org.spekframework.spek2.runtime.execution.ExecutionListener
import org.spekframework.spek2.runtime.execution.ExecutionResult
import org.spekframework.spek2.runtime.scope.ActionScopeImpl
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.TestScopeImpl

abstract class TrackingExecutionListener: ExecutionListener {
    protected var groupsExecuted = 0
    protected var actionsExecuted = 0
    protected var testsExecuted = 0

    protected var groupsIgnored = 0
    protected var actionsIgnored = 0
    protected var testsIgnored = 0

    protected var groupsFailed = 0
    protected var actionsFailed = 0
    protected var testsFailed = 0

    override fun executionStart() {
        groupsExecuted = 0
        groupsFailed = 0
        groupsIgnored = 0

        testsExecuted = 0
        testsFailed = 0
        testsIgnored = 0

        actionsExecuted = 0
        actionsFailed = 0
        actionsIgnored = 0
    }

    override fun testExecutionStart(test: TestScopeImpl) {
        testsExecuted++
    }

    override fun testExecutionFinish(test: TestScopeImpl, result: ExecutionResult) {
        if (result is ExecutionResult.Failure) {
            testsFailed++
        }
    }

    override fun testIgnored(test: TestScopeImpl, reason: String?) {
        testsIgnored++
    }

    override fun groupExecutionStart(group: GroupScopeImpl) {
        groupsExecuted++
    }

    override fun groupExecutionFinish(group: GroupScopeImpl, result: ExecutionResult) {
        if (result is ExecutionResult.Failure) {
            groupsFailed++
        }
    }

    override fun groupIgnored(group: GroupScopeImpl, reason: String?) {
        groupsIgnored++
    }

    override fun actionExecutionStart(action: ActionScopeImpl) {
        actionsExecuted++
    }

    override fun actionExecutionFinish(action: ActionScopeImpl, result: ExecutionResult) {
        if (result is ExecutionResult.Failure) {
            actionsFailed++
        }
    }

    override fun actionIgnored(action: ActionScopeImpl, reason: String?) {
        actionsIgnored++
    }
}
