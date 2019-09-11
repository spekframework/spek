package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.ExecutionResult
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope

class LifecycleManager {

    private val listeners = mutableListOf<LifecycleListener>()

    fun addListener(listener: LifecycleListener) {
        require(!listeners.contains(listener)) { "You can only register a listener once." }
        listeners.add(listener)
    }

    fun beforeExecuteTest(test: TestScope) {
        listeners.forEach { it.beforeExecuteTest(test) }
    }

    fun afterExecuteTest(test: TestScope, result: ExecutionResult) {
        listeners.reversed().forEach { it.afterExecuteTest(test, result) }
    }

    fun beforeExecuteGroup(group: GroupScope) {
        listeners.forEach { it.beforeExecuteGroup(group) }
    }

    fun afterExecuteGroup(group: GroupScope, result: ExecutionResult) {
        listeners.reversed().forEach { it.afterExecuteGroup(group, result) }
    }
}
