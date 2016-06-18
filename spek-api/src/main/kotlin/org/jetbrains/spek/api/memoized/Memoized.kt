package org.jetbrains.spek.api.memoized

import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
interface Memoized<T> {
    operator fun getValue(ref: Any?, property: KProperty<*>): T
}
