package org.jetbrains.spek.api.subject

import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface Subject<T> {
    operator fun getValue(ref: Any?, property: KProperty<*>): T
}
