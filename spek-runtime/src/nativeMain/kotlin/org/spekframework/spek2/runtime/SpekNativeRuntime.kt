package org.spekframework.spek2.runtime

import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.scope.PathBuilder

actual class SpekRuntime : AbstractRuntime() {
    // TODO: support filtering list of scopes like the JVM runtime does
    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes =  discoveryRequest.classes
                .mapKeys { (klass, _) -> PathBuilder.from(klass).build() }
                .mapValues { (path, factory) -> resolveSpec(factory(), path) }
                .map { (path, instance) -> instance.apply { filterBy(path) } }
                .filter { !it.isEmpty() }

        return DiscoveryResult(scopes)
    }
}

