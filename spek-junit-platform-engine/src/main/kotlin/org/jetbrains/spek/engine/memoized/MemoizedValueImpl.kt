package org.jetbrains.spek.engine.memoized

import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.api.memoized.MemoizedValue
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
open class MemoizedValueImpl<T>(val mode: CachingMode, val factory: () -> T): MemoizedValue<T> {
    private var instance: T? = null

    fun get(): T {
        if (instance == null) {
            instance = factory()
        }
        return instance!!
    }

    override operator fun getValue(ref: Any?, property: KProperty<*>) = get()

    fun reset() {
        instance = null
    }
}
