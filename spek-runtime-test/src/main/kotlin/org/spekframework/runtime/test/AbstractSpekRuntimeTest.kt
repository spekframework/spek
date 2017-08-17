package org.spekframework.runtime.test

import org.jetbrains.spek.api.Spek
import org.spekframework.runtime.SpekRuntime
import org.spekframework.runtime.execution.DiscoveryRequest
import org.spekframework.runtime.execution.ExecutionRequest
import org.spekframework.runtime.scope.Path
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
abstract class AbstractSpekRuntimeTest<out T: SpekRuntime>(protected val runtime: T) {

    protected abstract fun toPath(spek: KClass<out Spek>): Path
    protected fun executeTestsForClass(spek: KClass<out Spek>) = executeTestsforPath(toPath(spek))

    protected fun executeTestsforPath(path: Path): ExecutionEventRecorder {
        return ExecutionEventRecorder().apply {
            val discoveryRequest = DiscoveryRequest(path)
            val discoveryResult = runtime.discover(discoveryRequest)
            val executionRequest = ExecutionRequest(discoveryResult.roots, this)
            runtime.execute(executionRequest)
        }
    }
}
