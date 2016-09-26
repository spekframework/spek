package org.jetbrains.spek.engine

import org.jetbrains.spek.api.lifecycle.GroupScope
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import org.junit.platform.engine.support.hierarchical.Node

/**
 * @author Ranie Jade Ramiso
 */
class SpekEngineDescriptor(uniqueId: UniqueId)
    : EngineDescriptor(uniqueId, "Spek"), Node<SpekExecutionContext>, GroupScope {
    override val parent = null
}
