package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import org.spekframework.spek2.runtime.scope.ScopeImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

sealed class MemoizedValueAdapter<T>(
    val factory: () -> T,
    val destructor: (T) -> Unit,
    val eager: Boolean
) : ReadOnlyProperty<Any?, T>, LifecycleListener {

    protected sealed class Cached<out T> {
        object Empty : Cached<Nothing>()
        data class Value<out T>(val value: T) : Cached<T>()
    }

    protected var cached: Cached<T> = Cached.Empty

    override fun getValue(thisRef: Any?, property: KProperty<*>) = get()

    fun get(): T {
        val cached = this.cached
        return when (cached) {
            Cached.Empty -> {
                val newCached = Cached.Value(factory())
                this.cached = newCached
                newCached.value
            }
            is Cached.Value<T> -> cached.value
        }
    }

    class GroupCachingModeAdapter<T>(
            factory: () -> T,
            destructor: (T) -> Unit,
            eager: Boolean
    ) : MemoizedValueAdapter<T>(factory, destructor, eager) {

        private val stack = mutableListOf<Cached<T>>()

        override fun beforeExecuteGroup(group: GroupScope) {
            stack.add(0, cached)

            if (eager) {
                cached = Cached.Value(factory())
            } else {
                cached = Cached.Empty
            }
        }

        override fun afterExecuteGroup(group: GroupScope) {
            val cached = this.cached
            if (cached is Cached.Value<T>) {
                destructor(cached.value)
            }
            if (stack.isNotEmpty()) {
                this.cached = stack.removeAt(0)
            }
        }
    }

    class ScopeCachingModeAdapter<T>(
            val scope: ScopeImpl,
            factory: () -> T, destructor: (T) -> Unit, eager: Boolean
    ) : MemoizedValueAdapter<T>(factory, destructor, eager) {

        override fun beforeExecuteGroup(group: GroupScope) {
            if (eager) {
                if (this.scope == group) {
                    this.cached = Cached.Value(factory())
                }
            }
        }

        override fun afterExecuteGroup(group: GroupScope) {
            if (this.scope == group) {
                val cached = this.cached
                when (cached) {
                    is Cached.Value<T> -> destructor(cached.value)
                }
                this.cached = Cached.Empty
            }
        }
    }

    class TestCachingModeAdapter<T>(
            factory: () -> T,
            destructor: (T) -> Unit,
            eager: Boolean
    ) : MemoizedValueAdapter<T>(factory, destructor, eager) {

        override fun beforeExecuteTest(test: TestScope) {
            if (eager) {
                this.cached = Cached.Value(factory())
            }
        }

        override fun afterExecuteTest(test: TestScope) {
            val cached = this.cached
            when (cached) {
                is Cached.Value<T> -> destructor(cached.value)
            }
            this.cached = Cached.Empty
        }
    }
}
