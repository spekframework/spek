package org.spekframework.runtime.execution

import org.jetbrains.spek.api.Spek
import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.scope.Scope
import kotlin.reflect.KClass

data class DiscoveryRequest internal constructor(
    val packages: List<String>,
    val specs: List<KClass<out Spek>>,
    val path: Path?
) {
    class Builder internal constructor() {
        var packages = mutableListOf<String>()
        var specs = mutableListOf<KClass<out Spek>>()
        var path: Path? = null

        internal fun build(): DiscoveryRequest {
            return DiscoveryRequest(packages.toList(), specs.toList(), path)
        }
    }

    companion object {
        fun create(setup: Builder.() -> Unit): DiscoveryRequest {
            return Builder().run {
                setup()
                build()
            }
        }
    }
}

class DiscoveryResult(val roots: List<Scope>)

