package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental
import kotlin.properties.ReadOnlyProperty

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
interface LifecycleAware<out T>: ReadOnlyProperty<Any?, T> {
    operator fun invoke(): T
}
