package org.jetbrains.spek.api.memoized

import org.jetbrains.spek.api.annotation.Beta
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
@Beta
interface MemoizedValue<T> {
    operator fun getValue(ref: Any?, property: KProperty<*>): T
}
