package org.jetbrains.spek.engine

import org.jetbrains.spek.api.extension.Extension
import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext

/**
 * @author Ranie Jade Ramiso
 */
class SpekExecutionContext(private val registry: ExtensionRegistryImpl): EngineExecutionContext {
    fun extensions(): Sequence<Extension> = registry.extensions()
}
