package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.meta.Experimental
import org.spekframework.spek2.meta.SpekDsl

/**
 * @since 1.1
 */
@Experimental
@SpekDsl
interface Root: GroupBody {
    fun registerListener(listener: LifecycleListener)
}
