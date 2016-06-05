package org.jetbrains.spek.engine

import org.jetbrains.spek.engine.scope.Scope
import org.junit.gen5.engine.support.hierarchical.EngineExecutionContext
import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class SpekExecutionContext: EngineExecutionContext {
    private val listeners = LinkedHashSet<ExecutionListener>()

    fun addListener(listener: ExecutionListener) {
        listeners.add(listener)
    }

    fun beforeTest(scope: Scope.Test) {
        listeners.forEach { it.beforeTest(scope) }
    }
    fun afterTest(scope: Scope.Test) {
        listeners.forEach { it.afterTest(scope) }

    }
}
