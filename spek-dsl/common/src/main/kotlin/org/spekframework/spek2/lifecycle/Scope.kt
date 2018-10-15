package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental

/**
 * @since 1.1
 */
@Experimental
interface Scope {
    val parent: GroupScope?
}
