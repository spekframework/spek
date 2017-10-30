package org.spekframework.spek2.junit

import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.ScopeImpl
import org.junit.platform.engine.ExecutionRequest as JUnitExecutionRequest

class SpekTestEngine: TestEngine {
    val factory = TestDescriptorAdapterFactory()
    val runtime by lazy { SpekRuntime() }

    override fun getId() = "spek"

    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val engineDescriptor = SpekEngineDescriptor(uniqueId, id)

        val pathSelector = discoveryRequest.getSelectorsByType(PathSelector::class.java)
            .firstOrNull() ?: PathSelector(PathBuilder.ROOT)

        val discoveryResult = runtime.discover(DiscoveryRequest(pathSelector.path))

        discoveryResult.roots
            .map(this::toTestDescriptor)
            .forEach(engineDescriptor::addChild)

        return engineDescriptor
    }

    override fun execute(request: JUnitExecutionRequest) {
        val roots = request.rootTestDescriptor.children
            .filterIsInstance<TestDescriptorAdapter>()
            .map(TestDescriptorAdapter::scope)

        val runtimeExecutionRequest = ExecutionRequest(
            roots, RuntimeExecutionListenerAdapter(request.engineExecutionListener, factory)
        )

        runtime.execute(runtimeExecutionRequest)
    }

    private fun toTestDescriptor(root: ScopeImpl): TestDescriptor {
        return factory.create(root)
    }
}
