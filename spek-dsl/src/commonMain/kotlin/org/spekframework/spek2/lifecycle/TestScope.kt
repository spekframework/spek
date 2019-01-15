package org.spekframework.spek2.lifecycle

interface TestScope : Scope {
    override val parent: GroupScope
}
