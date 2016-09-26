package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.meta.SpekDsl

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@SpekDsl
interface SpecBody: TestContainer {
    fun group(description: String, pending: Pending = Pending.No, body: SpecBody.() -> Unit)
    fun action(description: String, pending: Pending = Pending.No, body: ActionBody.() -> Unit)
    fun <T> memoized(mode: CachingMode = CachingMode.TEST, factory: () -> T): LifecycleAware<T>

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)
}
