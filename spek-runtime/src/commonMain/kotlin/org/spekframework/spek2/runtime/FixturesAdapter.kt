package org.spekframework.spek2.runtime

import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope

class FixturesAdapter : LifecycleListener {

    private val beforeEachTest: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()
    private val afterEachTest: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()

    private val beforeGroup: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()
    private val afterGroup: LinkedHashMap<GroupScope, MutableList<() -> Unit>> = LinkedHashMap()

    override fun beforeExecuteTest(test: TestScope) {
        invokeAllBeforeEachTest(test.parent)
    }

    override fun afterExecuteTest(test: TestScope) {
        invokeAllAfterEachTest(test.parent)
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
        group.parent?.let {
            invokeAllBeforeEachTest(it)
        }

        beforeEachTest[group]?.forEach { it.invoke() }
    }

    private fun invokeAllAfterEachTest(group: GroupScope) {
        afterEachTest[group]?.reversed()?.forEach { it.invoke() }

        group.parent?.let {
            invokeAllAfterEachTest(it)
        }
    }
}
