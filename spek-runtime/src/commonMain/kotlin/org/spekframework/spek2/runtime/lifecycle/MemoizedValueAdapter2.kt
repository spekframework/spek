package org.spekframework.spek2.runtime.lifecycle

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MemoizedValueAdapter2<T>(
    private val factory: () -> T,
    private val destructor: (T) -> Unit
): ReadOnlyProperty<Any?, T> {
    private var value: T?  = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (value != null) value as T else throw AssertionError("Memoized value is null!")
    }

    fun init() {
        value = factory()
    }

    fun destroy() {
        value?.let(destructor)
        value = null
    }
}
