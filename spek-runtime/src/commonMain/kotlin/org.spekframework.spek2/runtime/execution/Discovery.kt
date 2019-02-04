package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.ScopeImpl

expect class DiscoveryRequest

data class DiscoveryResult(val roots: List<ScopeImpl>)

