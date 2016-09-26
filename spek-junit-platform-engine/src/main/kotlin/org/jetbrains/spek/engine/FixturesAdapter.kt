package org.jetbrains.spek.engine

import org.jetbrains.spek.api.lifecycle.ActionScope
import org.jetbrains.spek.api.lifecycle.GroupScope
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.api.lifecycle.TestScope
import java.util.LinkedList
import java.util.WeakHashMap

/**
 *
 * @author Ranie Jade Ramiso
 */
class FixturesAdapter: LifecycleListener {
    private val beforeEach: MutableMap<GroupScope, MutableList<() -> Unit>> = WeakHashMap()

    private val afterEach: MutableMap<GroupScope, MutableList<() -> Unit>> = WeakHashMap()

    override fun beforeExecuteTest(test: TestScope) {
        if (test.parent !is ActionScope) {
            invokeAllBeforeEach(test.parent)
        }
    }

    override fun afterExecuteTest(test: TestScope) {
        if (test.parent !is ActionScope) {
            invokeAllAfterEach(test.parent)
        }
    }

    override fun beforeExecuteAction(action: ActionScope) {
        invokeAllBeforeEach(action)
    }

    override fun afterExecuteAction(action: ActionScope) {
        invokeAllAfterEach(action)
    }


    fun registerBeforeEach(group: GroupScope, callback: () -> Unit) {
        beforeEach.getOrPut(group, { LinkedList() }).add(callback)
    }

    fun registerAfterEach(group: GroupScope, callback: () -> Unit) {
        afterEach.getOrPut(group, { LinkedList() }).add(callback)
    }

    private fun invokeAllBeforeEach(group: GroupScope) {
        if (group.parent != null) {
            invokeAllBeforeEach(group.parent!!)
        }
        beforeEach[group]?.forEach { it.invoke() }
    }

    private fun invokeAllAfterEach(group: GroupScope) {
        afterEach[group]?.forEach { it.invoke() }
        if (group.parent != null) {
            invokeAllAfterEach(group.parent!!)
        }
    }
}
