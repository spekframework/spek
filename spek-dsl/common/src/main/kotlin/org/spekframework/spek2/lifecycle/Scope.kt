package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental

@Experimental
interface Scope {
    val parent: GroupScope?
}
