package org.jetbrains.spek.engine

import org.jetbrains.spek.engine.scope.Scope

/**
 * @author Ranie Jade Ramiso
 */
interface ExecutionListener {
    fun beforeTest(test: Scope.Test) { }
    fun afterTest(test: Scope.Test) { }
}
