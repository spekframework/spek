package org.jetbrains.spek.engine

import org.jetbrains.spek.api.extension.GroupExtensionContext
import org.jetbrains.spek.api.extension.TestExtensionContext
import org.jetbrains.spek.api.extension.execution.AfterExecuteTest
import org.jetbrains.spek.api.extension.execution.BeforeExecuteTest
import java.util.*

/**
 * Adapter for fixtures as a pseudo-extension.
 *
 * @author Ranie Jade Ramiso
 */
class FixturesAdapter: BeforeExecuteTest, AfterExecuteTest {
    private val beforeEach: MutableMap<GroupExtensionContext, MutableList<() -> Unit>> = WeakHashMap()
    private val afterEach: MutableMap<GroupExtensionContext, MutableList<() -> Unit>> = WeakHashMap()

    override fun beforeExecuteTest(test: TestExtensionContext) {
        invokeAllBeforeEach(test.parent)
    }

    override fun afterExecuteTest(test: TestExtensionContext) {
        invokeAllAfterEach(test.parent)
    }

    fun registerBeforeEach(group: GroupExtensionContext, callback: () -> Unit) {
        beforeEach.getOrPut(group, { LinkedList() }).add(callback)
    }

    fun registerAfterEach(group: GroupExtensionContext, callback: () -> Unit) {
        afterEach.getOrPut(group, { LinkedList() }).add(callback)
    }

    private fun invokeAllBeforeEach(group: GroupExtensionContext) {
        if (group.parent != null) {
            invokeAllBeforeEach(group.parent!!)
        }
        beforeEach[group]?.forEach { it.invoke() }
    }

    private fun invokeAllAfterEach(group: GroupExtensionContext) {
        afterEach[group]?.forEach { it.invoke() }
        if (group.parent != null) {
            invokeAllAfterEach(group.parent!!)
        }
    }
}
