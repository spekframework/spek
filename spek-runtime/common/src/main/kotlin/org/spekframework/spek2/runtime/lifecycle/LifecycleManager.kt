package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope

/**
 * @author Ranie Jade Ramiso
 */
class LifecycleManager {
    private val listeners = ArrayList<LifecycleListener>()

    fun addListener(listener: LifecycleListener) {
        if (listeners.contains(listener)) {
            throw IllegalArgumentException("You can only register a listener once.")

        }

        listeners.add(0, listener)
    }

    fun removeListener(listener: LifecycleListener) {
        listeners.remove(listener)
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

    fun beforeExecuteAction(action: ActionScope) {
        listeners.forEach { it.beforeExecuteAction(action) }
    }

    fun afterExecuteAction(action: ActionScope) {
        listeners.reversed().forEach { it.afterExecuteAction(action) }
    }
}
