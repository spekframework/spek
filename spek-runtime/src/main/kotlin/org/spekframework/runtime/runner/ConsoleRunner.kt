package org.spekframework.runtime.runner

import org.spekframework.runtime.SpekRuntime
import org.spekframework.runtime.execution.DiscoveryRequest
import org.spekframework.runtime.execution.ExecutionRequest

abstract class ConsoleRunner {
    val runtime by lazy { createRuntime() }
    abstract protected fun createRuntime(): SpekRuntime

    fun execute(config: RunConfig) {
        val discoveryResult = runtime.discover(DiscoveryRequest(config.path))
        runtime.execute(ExecutionRequest(discoveryResult.roots, config.recorder))
    }
}
