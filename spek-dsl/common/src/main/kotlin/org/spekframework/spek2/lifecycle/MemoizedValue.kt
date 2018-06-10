package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Experimental
interface MemoizedValue<out T> {
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, T>
}
