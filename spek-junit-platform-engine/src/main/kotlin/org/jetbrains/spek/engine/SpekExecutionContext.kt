package org.jetbrains.spek.engine

import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext

/**
 * @author Ranie Jade Ramiso
 */
class SpekExecutionContext(val registry: ExtensionRegistryImpl,
                           val executionRequest: ExecutionRequest): EngineExecutionContext {
    val engineExecutionListener: EngineExecutionListener
        get() = executionRequest.engineExecutionListener
}
