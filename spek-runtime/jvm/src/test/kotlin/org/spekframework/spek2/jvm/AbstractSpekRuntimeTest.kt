package org.spekframework.spek2.jvm

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import kotlin.reflect.KClass

abstract class AbstractSpekRuntimeTest {

    private val runtime = SpekRuntime()

    private fun toPath(spek: KClass<out Spek>): Path = PathBuilder.from(spek).build()

    protected fun executeTestsForClass(spek: KClass<out Spek>) = executeTestsForPath(toPath(spek))

    protected fun executeTestsForPath(path: Path): ExecutionEventRecorder = ExecutionEventRecorder().apply {
        val discoveryRequest = DiscoveryRequest(emptyList(), listOf(path))
        val discoveryResult = runtime.discover(discoveryRequest)
        val executionRequest = ExecutionRequest(discoveryResult.roots, this)

        runtime.execute(executionRequest)
    }
}
