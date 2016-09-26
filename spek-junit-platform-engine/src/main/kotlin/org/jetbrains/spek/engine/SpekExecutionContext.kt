package org.jetbrains.spek.engine

import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext

/**
 * @author Ranie Jade Ramiso
 */
class SpekExecutionContext(val executionRequest: ExecutionRequest): EngineExecutionContext {
    val engineExecutionListener: EngineExecutionListener
        get() = executionRequest.engineExecutionListener
}
