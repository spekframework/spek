package org.spekframework.runtime

import org.spekframework.runtime.execution.DiscoveryRequest
import org.spekframework.runtime.execution.DiscoveryResult
import org.spekframework.runtime.execution.ExecutionListener
import org.spekframework.runtime.execution.ExecutionRequest

abstract class SpekRuntime {
    private val listeners = mutableListOf<ExecutionListener>()

    abstract fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult

    fun execute(executionRequest: ExecutionRequest) {
        TODO()
    }

    fun addListener(listener: ExecutionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ExecutionListener) {
        listeners.remove(listener)
    }
}
