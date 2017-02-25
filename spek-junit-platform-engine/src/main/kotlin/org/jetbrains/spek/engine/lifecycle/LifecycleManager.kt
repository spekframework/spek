package org.jetbrains.spek.engine.lifecycle

import org.jetbrains.spek.api.lifecycle.ActionScope
import org.jetbrains.spek.api.lifecycle.GroupScope
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.api.lifecycle.TestScope
import java.util.ArrayList

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
        listeners.forEach { it.afterExecuteTest(test) }
    }

    fun beforeExecuteGroup(group: GroupScope) {
        listeners.forEach { it.beforeExecuteGroup(group) }
    }

    fun afterExecuteGroup(group: GroupScope) {
        listeners.forEach { it.afterExecuteGroup(group) }
    }

    fun beforeExecuteAction(action: ActionScope) {
        listeners.forEach { it.beforeExecuteAction(action) }
    }

    fun afterExecuteAction(action: ActionScope) {
        listeners.forEach { it.afterExecuteAction(action) }
    }
}
