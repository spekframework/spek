package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.ActionScope
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import kotlin.reflect.KProperty

sealed class LifecycleAwareAdapter<T>(val factory: () -> T, val destructor: (T) -> Unit)
    : LifecycleAware<T>, LifecycleListener {

    protected sealed class Cached<out T> {
        object Empty : Cached<Nothing>()
        data class Value<out T>(val value: T) : Cached<T>()
    }

    protected var cached: Cached<T> = Cached.Empty

    override fun getValue(thisRef: Any?, property: KProperty<*>) = invoke()

    override fun invoke(): T = when(cached) {
        Cached.Empty -> {
            val newCached = Cached.Value(factory())
            cached = newCached
            newCached.value
        }
        is Cached.Value<T> -> cached.cast().value
    }

    protected fun Cached<T>.cast() = this as Cached.Value<T>

    class GroupCachingModeAdapter<T>(factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        private val stack = mutableListOf<Cached<T>>()

        override fun beforeExecuteGroup(group: GroupScope) {
            stack.add(0, cached)
            cached = Cached.Empty
        }

        override fun afterExecuteGroup(group: GroupScope) {
            if (cached is Cached.Value<T>) {
                destructor(cached.cast().value)
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
                when (cached) {
                    is Cached.Value<T> -> destructor(cached.cast().value)
                }
                cached = Cached.Empty
            }
        }
    }

    class TestCachingModeAdapter<T>(factory: () -> T, destructor: (T) -> Unit)
        : LifecycleAwareAdapter<T>(factory, destructor) {
        override fun afterExecuteTest(test: TestScope) {
            if (test.parent !is ActionScope) {
                when (cached) {
                    is Cached.Value<T> -> destructor(cached.cast().value)
                }
                cached = Cached.Empty
            }
        }

        override fun afterExecuteAction(action: ActionScope) {
            when (cached) {
                is Cached.Value<T> -> destructor(cached.cast().value)
            }
            cached = Cached.Empty
        }
    }
}
