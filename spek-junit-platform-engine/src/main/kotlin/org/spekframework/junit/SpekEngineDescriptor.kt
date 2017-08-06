package org.spekframework.junit

import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class SpekEngineDescriptor(
    uniqueId: UniqueId,
    name: String
): EngineDescriptor(uniqueId, name)
