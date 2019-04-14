package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.scope.ScopeImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MemoizedValueCreator<out T>(
    val scope: ScopeImpl,
    private val mode: CachingMode,
    val factory: () -> T,
    private val destructor: (T) -> Unit
) : MemoizedValue<T> {

    override operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ReadOnlyProperty<Any?, T> {

        val adapter = when (mode) {
            CachingMode.GROUP, CachingMode.EACH_GROUP -> MemoizedValueAdapter.GroupCachingModeAdapter(factory, destructor)
            CachingMode.TEST -> MemoizedValueAdapter.TestCachingModeAdapter(factory, destructor)
            CachingMode.SCOPE -> MemoizedValueAdapter.ScopeCachingModeAdapter(scope, factory, destructor)
            CachingMode.INHERIT -> throw AssertionError("Not allowed.")
        }

        // reserve name
        scope.registerValue(property.name, adapter)

        return adapter.apply {
            scope.lifecycleManager.addListener(this)
        }
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
