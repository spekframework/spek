package org.spekframework.spek2.runtime

import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.scope.isRelated

actual class SpekRuntime : AbstractRuntime() {
    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = discoveryRequest.context.getTests()
            .map { testInfo ->
                val matchingPath = discoveryRequest.paths.firstOrNull { it.isRelated(testInfo.path) }
                testInfo to matchingPath
            }
            .filter { (_, matchingPath) -> matchingPath != null }
            .map { (testInfo, matchingPath) ->
                checkNotNull(matchingPath)
                val spec = resolveSpec(testInfo.createInstance(), testInfo.path)
                spec.filterBy(matchingPath)
                spec
            }
            .filter { spec -> !spec.isEmpty() }


        return DiscoveryResult(scopes)
    }
}
