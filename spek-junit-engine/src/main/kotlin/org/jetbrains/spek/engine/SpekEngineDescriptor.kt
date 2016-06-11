package org.jetbrains.spek.engine

import org.junit.gen5.engine.UniqueId
import org.junit.gen5.engine.support.descriptor.EngineDescriptor
import org.junit.gen5.engine.support.hierarchical.Node

/**
 * @author Ranie Jade Ramiso
 */
class SpekEngineDescriptor(uniqueId: UniqueId): EngineDescriptor(uniqueId, "Spek"), Node<SpekExecutionContext>
