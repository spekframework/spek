package org.spekframework.spek2.runtime

import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope

/**
 *
 * @author Ranie Jade Ramiso
 */
class FixturesAdapter: LifecycleListener {
    private val beforeEachTest: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()
    private val afterEachTest: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()

    private val beforeGroup: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()
    private val afterGroup: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()

    override fun beforeExecuteTest(test: TestScope) {
        if (test.parent !is ActionScope) {
            invokeAllBeforeEachTest(test.parent)
        }
    }

    override fun afterExecuteTest(test: TestScope) {
        if (test.parent !is ActionScope) {
            invokeAllAfterEachTest(test.parent)
        }
    }

    override fun beforeExecuteAction(action: ActionScope) {
        invokeAllBeforeEachTest(action)
    }

    override fun afterExecuteAction(action: ActionScope) {
        invokeAllAfterEachTest(action)
    }

    override fun beforeExecuteGroup(group: GroupScope) {
        beforeGroup[group]?.forEach { it() }
    }

    override fun afterExecuteGroup(group: GroupScope) {
        afterGroup[group]?.reversed()?.forEach { it() }
    }

    fun registerBeforeEachTest(group: GroupScope, callback: () -> Unit) {
        beforeEachTest.getOrPut(group, { mutableListOf() }).add(callback)
    }

    fun registerAfterEachTest(group: GroupScope, callback: () -> Unit) {
        afterEachTest.getOrPut(group, { mutableListOf() }).add(callback)
    }

    fun registerBeforeGroup(group: GroupScope, callback: () -> Unit) {
        beforeGroup.getOrPut(group, { mutableListOf() }).add(callback)
    }

    fun registerAfterGroup(group: GroupScope, callback: () -> Unit) {
        afterGroup.getOrPut(group, { mutableListOf() }).add(callback)
    }

    private fun invokeAllBeforeEachTest(group: GroupScope) {
        if (group.parent != null) {
            invokeAllBeforeEachTest(group.parent!!)
        }
        beforeEachTest[group]?.forEach { it.invoke() }
    }

    private fun invokeAllAfterEachTest(group: GroupScope) {
        afterEachTest[group]?.reversed()?.forEach { it.invoke() }
        if (group.parent != null) {
            invokeAllAfterEachTest(group.parent!!)
        }
    }
}
