package org.jetbrains.spek.api.memoized

import org.jetbrains.spek.meta.Experimental
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
interface MemoizedValue<T> {
    operator fun getValue(ref: Any?, property: KProperty<*>): T
}
