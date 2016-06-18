package org.jetbrains.spek.engine.memoized

import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.api.memoized.Memoized
import org.jetbrains.spek.engine.ExecutionListener
import org.jetbrains.spek.engine.scope.Scope
import kotlin.reflect.KProperty

/**
 * @author Ranie Jade Ramiso
 */
open class MemoizedHelper<T>(val mode: CachingMode, val factory: () -> T): ExecutionListener, Memoized<T> {
    private var instance: T? = null

    fun get(): T {
        if (instance == null) {
            instance = factory()
        }
        return instance!!
    }

    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = get()

    override fun beforeTest(test: Scope.Test) {
        if (mode == CachingMode.TEST) {
            instance = null
        }
    }

}
