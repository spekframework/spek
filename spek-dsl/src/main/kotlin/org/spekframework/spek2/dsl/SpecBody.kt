package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.LifecycleAware
import org.spekframework.spek2.meta.SpekDsl

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

    fun beforeGroup(callback: () -> Unit)
    fun afterGroup(callback: () -> Unit)
}
