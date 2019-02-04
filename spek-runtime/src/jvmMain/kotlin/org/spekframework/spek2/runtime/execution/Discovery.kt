package org.spekframework.spek2.runtime.execution

import org.spekframework.spek2.runtime.scope.Path

actual data class DiscoveryRequest(val sourceDirs: List<String>, val paths: List<Path>)
