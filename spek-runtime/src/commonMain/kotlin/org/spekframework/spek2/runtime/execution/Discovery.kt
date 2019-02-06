package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.ScopeImpl

class DiscoveryRequest(val context: DiscoveryContext, val paths: List<Path>)
class DiscoveryResult(val roots: List<ScopeImpl>)