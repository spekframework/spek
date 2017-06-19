package org.spekframework.runtime

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.lifecycle.InstanceFactory
import org.spekframework.runtime.execution.DiscoveryRequest
import org.spekframework.runtime.execution.ExecutionListener
import org.spekframework.runtime.execution.ExecutionRequest
import kotlin.reflect.KClass

abstract class SpekRuntime {
    private val listeners = mutableListOf<ExecutionListener>()

    fun discover(discoveryRequest: DiscoveryRequest): ExecutionRequest {
        TODO()
    }

    fun execute(executionRequest: ExecutionRequest) {
        TODO()
    }

    fun addListener(listener: ExecutionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ExecutionListener) {
        listeners.remove(listener)
    }


    abstract val defaultInstanceFactory: InstanceFactory

    protected abstract fun listSpecsForPackage(`package`: String): List<KClass<out Spek>>
}
