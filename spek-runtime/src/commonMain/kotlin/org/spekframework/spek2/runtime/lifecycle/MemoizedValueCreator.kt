package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.scope.ScopeImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MemoizedValueCreator<out T>(
    val scope: ScopeImpl,
    private val mode: CachingMode,
    private val lifecycleAware: LifecycleAware,
    val factory: suspend () -> T,
    private val destructor: suspend (T) -> Unit
) : MemoizedValue<T> {

    override operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ReadOnlyProperty<Any?, T> {

        val adapter = when (mode) {
            CachingMode.EACH_GROUP -> MemoizedValueAdapter.GroupCachingModeAdapter(property.name, factory, destructor)
            CachingMode.TEST -> MemoizedValueAdapter.TestCachingModeAdapter(property.name, factory, destructor)
            CachingMode.SCOPE -> MemoizedValueAdapter.ScopeCachingModeAdapter(scope, property.name, factory, destructor)
            CachingMode.INHERIT -> throw AssertionError("Not allowed.")
            else -> throw AssertionError("Should not happen!")
        }

        // reserve name
        scope.registerValue(property.name, adapter)

        adapter.setup(lifecycleAware)

        return adapter
    }
}

class MemoizedValueReader<out T>(val scope: ScopeImpl) : MemoizedValue<T> {

    @Suppress("UNCHECKED_CAST")
    override operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ReadOnlyProperty<Any?, T> {
        return scope.getValue(property.name) as ReadOnlyProperty<Any?, T>
    }
}
