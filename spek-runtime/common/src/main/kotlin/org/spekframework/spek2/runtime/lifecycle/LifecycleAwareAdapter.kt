package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import kotlin.reflect.KProperty

sealed class LifecycleAwareAdapter<T>(val factory: () -> T, val destructor: (T) -> Unit)
    : LifecycleAware<T>, LifecycleListener {
    protected data class CachedValue<out T>(val value: T)
    protected var cached: CachedValue<T>? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>) = invoke()

    override fun invoke(): T {
        if (cached == null) {
            cached = CachedValue(factory())
        }
        return cached!!.value
    }

    class GroupCachingModeAdapter<T>(factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        private val stack = mutableListOf<CachedValue<T>?>()

        override fun beforeExecuteGroup(group: GroupScope) {
            stack.add(0, cached)
            cached = null
        }

        override fun afterExecuteGroup(group: GroupScope) {
            cached?.let { destructor(it.value) }
            if (stack.isNotEmpty()) {
                cached = stack.removeAt(0)
            }
        }
    }

    class ScopeCachingModeAdapter<T>(val group: GroupScope, factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        override fun afterExecuteGroup(group: GroupScope) {
            if (this.group == group) {
                cached?.let { destructor(it.value) }
                cached = null
            }
        }
    }

    class TestCachingModeAdapter<T>(factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        override fun afterExecuteTest(test: TestScope) {
            if (test.parent !is ActionScope) {
                cached?.let { destructor(it.value) }
                cached = null
            }
        }

        override fun afterExecuteAction(action: ActionScope) {
            cached?.let { destructor(it.value) }
            cached = null
        }
    }
}
