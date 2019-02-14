package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.scope.Path

actual class DiscoveryRequest actual constructor(actual val context: DiscoveryContext, actual val paths: List<Path>)