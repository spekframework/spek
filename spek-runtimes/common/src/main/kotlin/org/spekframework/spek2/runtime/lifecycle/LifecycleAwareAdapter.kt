package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
sealed class LifecycleAwareAdapter<T>(val factory: () -> T)
    : LifecycleAware<T>, LifecycleListener {
    var cached: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>) = invoke()

    override fun invoke(): T {
        if (cached == null) {
            cached = factory()
        }
        return cached!!
    }

    class GroupCachingModeAdapter<T>(factory: () -> T): LifecycleAwareAdapter<T>(factory) {
        val stack = mutableListOf<T?>()

        override fun beforeExecuteGroup(group: GroupScope) {
            stack.add(0, cached)
            cached = null
        }

        override fun afterExecuteGroup(group: GroupScope) {
            if (stack.isNotEmpty()) {
                cached = stack.removeAt(0)
            }
        }
    }

    class ScopeCachingModeAdapter<T>(val group: GroupScope, factory: () -> T): LifecycleAwareAdapter<T>(factory) {
        override fun afterExecuteGroup(group: GroupScope) {
            if (this.group == group) {
                cached = null
            }
        }
    }

    class TestCachingModeAdapter<T>(factory: () -> T): LifecycleAwareAdapter<T>(factory) {
        override fun afterExecuteTest(test: TestScope) {
            if (test.parent !is ActionScope) {
                cached = null
            }
        }

        override fun afterExecuteAction(action: ActionScope) {
            cached = null
        }
    }
}
