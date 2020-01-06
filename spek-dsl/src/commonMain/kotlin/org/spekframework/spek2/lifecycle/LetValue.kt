package org.spekframework.spek2.lifecycle

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface LetValue<out T>: ReadOnlyProperty<Any?, LetValue<T>> {
    operator fun invoke(): T

    interface PropertyCreator<out T> {
        operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, LetValue<T>>
    }
}
