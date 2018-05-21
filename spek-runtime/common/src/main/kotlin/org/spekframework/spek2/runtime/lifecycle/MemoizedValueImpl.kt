package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.scope.ScopeImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class MemoizedValueImpl<T>(val scope: ScopeImpl,
                           val mode: CachingMode,
                           val factory: () -> T,
                           val destructor: (T) -> Unit)
    : MemoizedValue<T> {
    override operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ReadOnlyProperty<Any?, T> {


        val adapter = when (mode) {
            CachingMode.GROUP -> MemoizedValueAdapter.GroupCachingModeAdapter(factory, destructor)
            CachingMode.TEST -> MemoizedValueAdapter.TestCachingModeAdapter(factory, destructor)
            CachingMode.SCOPE -> MemoizedValueAdapter.ScopeCachingModeAdapter(scope, factory, destructor)
        }

        // reserve name
        scope.registerValue(property.name, adapter)

        return adapter.apply {
            scope.lifecycleManager.addListener(this)
        }
    }
}
