package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.runtime.scope.ScopeImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

sealed class MemoizedValueAdapter<T>(
    val name: String,
    val factory: () -> T,
    val destructor: (T) -> Unit
) : ReadOnlyProperty<Any?, T> {

    protected sealed class Cached<out T> {
        object Invalid: Cached<Nothing>()
        object Empty : Cached<Nothing>()
        data class Value<out T>(val value: T) : Cached<T>()
    }

    protected var cached: Cached<T> = Cached.Invalid

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
            is Cached.Invalid -> throw AssertionError("'$name' can not be accessed in this context.")
        }
    }

    abstract fun setup(lifecycleAware: LifecycleAware)

    class GroupCachingModeAdapter<T>(
        name: String,
        factory: () -> T,
        destructor: (T) -> Unit
    ) : MemoizedValueAdapter<T>(name, factory, destructor) {

        private val stack = mutableListOf<Cached<T>>()

        override fun setup(lifecycleAware: LifecycleAware) {
            with(lifecycleAware) {
                beforeEachGroup {
                    stack.add(0, cached)
                    cached = Cached.Empty
                }

                afterEachGroup {
                    val cached = this@GroupCachingModeAdapter.cached
                    if (cached is Cached.Value<T>) {
                        destructor(cached.value)
                    }
                    this@GroupCachingModeAdapter.cached = if (stack.isNotEmpty()) {
                        stack.removeAt(0)
                    } else {
                        Cached.Invalid
                    }
                }
            }
        }
    }

    class ScopeCachingModeAdapter<T>(
        val scope: ScopeImpl,
        name: String,
        factory: () -> T,
        destructor: (T) -> Unit
    ) : MemoizedValueAdapter<T>(name, factory, destructor) {

        override fun setup(lifecycleAware: LifecycleAware) {
            with(lifecycleAware) {
                beforeGroup {
                    cached = Cached.Empty
                }

                afterGroup {
                    val cached = this@ScopeCachingModeAdapter.cached
                    when (cached) {
                        is Cached.Value<T> -> destructor(cached.value)
                        else -> {
                            // do nothing
                        }
                    }
                    this@ScopeCachingModeAdapter.cached = Cached.Invalid
                }
            }
        }
    }

    class TestCachingModeAdapter<T>(
        name: String,
        factory: () -> T,
        destructor: (T) -> Unit
    ) : MemoizedValueAdapter<T>(name, factory, destructor) {

        override fun setup(lifecycleAware: LifecycleAware) {
            with(lifecycleAware) {
                beforeEachTest {
                    cached = Cached.Empty
                }

                afterEachTest {
                    when (val cached = this@TestCachingModeAdapter.cached) {
                        is Cached.Value<T> -> destructor(cached.value)
                        else -> {
                            // do nothing
                        }
                    }
                    this@TestCachingModeAdapter.cached = Cached.Invalid
                }
            }
        }
    }
}
