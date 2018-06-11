package org.spekframework.spek2.lifecycle

import org.spekframework.spek2.meta.Experimental

@Experimental
interface TestScope : Scope {
    override val parent: GroupScope
}
