package org.spekframework.spek2.lifecycle

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface MemoizedValue<out T> {
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, T>
}
