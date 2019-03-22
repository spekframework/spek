package org.spekframework.spek2.runtime.lifecycle

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.runtime.scope.ScopeImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MemoizedValueCreator<out T>(
    private val root: Root,
    val scope: ScopeImpl,
    private val mode: CachingMode,
    val factory: () -> T,
    private val destructor: (T) -> Unit,
    val eager: Boolean
) : MemoizedValue<T> {

    override operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ReadOnlyProperty<Any?, T> {

        val adapter = when (mode) {
            CachingMode.GROUP -> GroupCachingModeAdapter(factory, destructor)
            CachingMode.TEST -> TestCachingModeAdapter(factory, destructor)
            CachingMode.SCOPE -> ScopeCachingModeAdapter(scope, factory, destructor)
            CachingMode.INHERIT -> throw AssertionError("Not allowed.")
        }

        // reserve name
        scope.registerValue(property.name, adapter)

        return adapter.apply {
            if (eager) {
                registerEagerInitializer(root)
            }

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
