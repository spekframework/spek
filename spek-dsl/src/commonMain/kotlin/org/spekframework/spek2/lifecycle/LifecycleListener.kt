package org.spekframework.spek2.lifecycle

interface LifecycleListener {
    fun beforeExecuteTest(test: TestScope) = Unit
    fun afterExecuteTest(test: TestScope, result: ExecutionResult) = Unit
    fun beforeExecuteGroup(group: GroupScope) = Unit
    fun afterExecuteGroup(group: GroupScope, result: ExecutionResult) = Unit
}
