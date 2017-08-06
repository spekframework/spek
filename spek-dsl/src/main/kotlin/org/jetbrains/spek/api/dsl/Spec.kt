package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
interface Spec: SpecBody {
    fun registerListener(listener: LifecycleListener)
}
