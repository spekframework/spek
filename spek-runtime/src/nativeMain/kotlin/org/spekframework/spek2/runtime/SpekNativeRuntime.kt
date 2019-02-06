package org.spekframework.spek2.runtime

import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.isRelated

actual class SpekRuntime : AbstractRuntime() {
    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = discoveryRequest.context.getTests()
            .map { it.path to { it.createInstance() } }
            .map { (path, factory) ->
                val matched = discoveryRequest.paths.firstOrNull { it.isRelated(path) }
                val root = matched?.let {
                    resolveSpec(factory(), path)
                }
                matched to root
            }
            .filter { (path, root) -> path != null && root != null }
            .map { (path, root) ->
                root!!.apply { filterBy(path!!) }
            }
            .filter { !it.isEmpty() }

        return DiscoveryResult(scopes)
    }
}

