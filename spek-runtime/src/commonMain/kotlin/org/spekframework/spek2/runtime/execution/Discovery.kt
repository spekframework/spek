package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.ScopeImpl

data class DiscoveryRequest(val context: DiscoveryContext, val sourceDirs: List<String>, val paths: List<Path>)
data class DiscoveryResult(val roots: List<ScopeImpl>)