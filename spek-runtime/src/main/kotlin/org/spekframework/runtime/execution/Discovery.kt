package org.spekframework.runtime.execution

import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.scope.ScopeImpl

data class DiscoveryRequest(val path: Path)
data class DiscoveryResult(val roots: List<ScopeImpl>)

