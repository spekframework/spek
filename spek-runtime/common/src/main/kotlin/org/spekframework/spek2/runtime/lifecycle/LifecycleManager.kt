package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope

class LifecycleManager {

    private val listeners = mutableListOf<LifecycleListener>()

    fun addListener(listener: LifecycleListener) {
        if (listeners.contains(listener)) {
            throw IllegalArgumentException("You can only register a listener once.")

        }

        listeners.add(0, listener)
    }

    fun beforeExecuteTest(test: TestScope) {
        listeners.forEach { it.beforeExecuteTest(test) }
    }

    fun afterExecuteTest(test: TestScope) {
        listeners.reversed().forEach { it.afterExecuteTest(test) }
    }

    fun beforeExecuteGroup(group: GroupScope) {
        listeners.forEach { it.beforeExecuteGroup(group) }
    }

    fun afterExecuteGroup(group: GroupScope) {
        listeners.reversed().forEach { it.afterExecuteGroup(group) }
    }
}
