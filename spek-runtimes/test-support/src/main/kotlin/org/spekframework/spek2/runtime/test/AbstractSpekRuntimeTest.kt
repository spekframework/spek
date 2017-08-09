package org.spekframework.spek2.runtime.test

import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.Path
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
