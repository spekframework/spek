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
    private val beforeEachTest: MutableMap<GroupScope, MutableList<() -> Unit>> = WeakHashMap()
    private val afterEachTest: MutableMap<GroupScope, MutableList<() -> Unit>> = WeakHashMap()

    private val beforeGroup: MutableMap<GroupScope, MutableList<() -> Unit>> = WeakHashMap()
    private val afterGroup: MutableMap<GroupScope, MutableList<() -> Unit>> = WeakHashMap()

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

    override fun beforeExecuteGroup(group: GroupScope) {
        beforeGroup[group]?.forEach { it() }
    }

    override fun afterExecuteGroup(group: GroupScope) {
        afterGroup[group]?.forEach { it() }
    }

    fun registerBeforeEachTest(group: GroupScope, callback: () -> Unit) {
        beforeEachTest.getOrPut(group, { LinkedList() }).add(callback)
    }

    fun registerAfterEachTest(group: GroupScope, callback: () -> Unit) {
        afterEachTest.getOrPut(group, { LinkedList() }).add(callback)
    }

    fun registerBeforeGroup(group: GroupScope, callback: () -> Unit) {
        beforeGroup.getOrPut(group, { LinkedList() }).add(callback)
    }

    fun registerAfterGroup(group: GroupScope, callback: () -> Unit) {
        afterGroup.getOrPut(group, { LinkedList() }).add(callback)
    }

    private fun invokeAllBeforeEach(group: GroupScope) {
        if (group.parent != null) {
            invokeAllBeforeEach(group.parent!!)
        }
        beforeEachTest[group]?.forEach { it.invoke() }
    }

    private fun invokeAllAfterEach(group: GroupScope) {
        afterEachTest[group]?.forEach { it.invoke() }
        if (group.parent != null) {
            invokeAllAfterEach(group.parent!!)
        }
    }
}
