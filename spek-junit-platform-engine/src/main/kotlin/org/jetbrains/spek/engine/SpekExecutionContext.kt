package org.jetbrains.spek.engine

import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext

/**
 * @author Ranie Jade Ramiso
 */
class SpekExecutionContext(val registry: ExtensionRegistryImpl): EngineExecutionContext
