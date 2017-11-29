package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.meta.Experimental

/**
 * @since 1.1
 */
@Experimental
interface Spec: SpecBody {
    fun registerListener(listener: LifecycleListener)
}
