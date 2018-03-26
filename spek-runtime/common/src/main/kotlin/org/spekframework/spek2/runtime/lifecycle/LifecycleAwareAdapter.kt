package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import kotlin.reflect.KProperty

sealed class LifecycleAwareAdapter<T>(val factory: () -> T, val destructor: (T) -> Unit)
    : LifecycleAware<T>, LifecycleListener {

    protected sealed class Cached {
        object Empty : Cached()
        data class Value<out T>(val value: T) : Cached()
    }

    protected fun <T> Cached.reified() = this as Cached.Value<T>

    protected var cached: Cached = Cached.Empty

    override fun getValue(thisRef: Any?, property: KProperty<*>) = invoke()

    override fun invoke(): T = when(cached) {
        Cached.Empty -> {
            cached = Cached.Value(factory())
            cached.reified<T>().value
        }
        is Cached.Value<*> -> cached.reified<T>().value
    }

    class GroupCachingModeAdapter<T>(factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        private val stack = mutableListOf<Cached>()

        override fun beforeExecuteGroup(group: GroupScope) {
            stack.add(0, cached)
            cached = Cached.Empty
        }

        override fun afterExecuteGroup(group: GroupScope) {
            if (cached != Cached.Empty) {
                destructor(cached.reified<T>().value)
            }
            if (stack.isNotEmpty()) {
                cached = stack.removeAt(0)
            }
        }
    }

    class ScopeCachingModeAdapter<T>(val group: GroupScope, factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        override fun afterExecuteGroup(group: GroupScope) {
            if (this.group == group) {
                if (cached != Cached.Empty) {
                    destructor(cached.reified<T>().value)
                }
                cached = Cached.Empty
            }
        }
    }

    class TestCachingModeAdapter<T>(factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        override fun afterExecuteTest(test: TestScope) {
            if (test.parent !is ActionScope) {
                if (cached != Cached.Empty) {
                    destructor(cached.reified<T>().value)
                }
                cached = Cached.Empty
            }
        }

        override fun afterExecuteAction(action: ActionScope) {
            if (cached != Cached.Empty) {
                destructor(cached.reified<T>().value)
            }
            cached = Cached.Empty
        }
    }
}
