package org.spekframework.spek2.junit

import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.ClasspathRootSelector
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.PathBuilder
import java.nio.file.Paths
import org.junit.platform.engine.ExecutionRequest as JUnitExecutionRequest

class SpekTestEngine : TestEngine {

    companion object {
        const val ID = "spek2"
    }

    private val descriptorFactory = SpekTestDescriptorFactory()
    private val runtime by lazy { SpekRuntime() }

    override fun getId() = ID

    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val engineDescriptor = SpekEngineDescriptor(uniqueId, id)

        val sourceDirs = discoveryRequest.getSelectorsByType(ClasspathRootSelector::class.java)
            .map { it.classpathRoot }
            .map { Paths.get(it) }
            .map { it.toString() }

        val pathSelector = discoveryRequest.getSelectorsByType(SpekPathDiscoverySelector::class.java)
            .firstOrNull() ?: SpekPathDiscoverySelector(PathBuilder.ROOT)

        val discoveryResult = runtime.discover(DiscoveryRequest(sourceDirs, listOf(pathSelector.path)))

        discoveryResult.roots
            .map { descriptorFactory.create(it) }
            .forEach(engineDescriptor::addChild)

        return engineDescriptor
    }

    override fun execute(request: JUnitExecutionRequest) {
        val roots = request.rootTestDescriptor.children
            .filterIsInstance<SpekTestDescriptor>()
            .map(SpekTestDescriptor::scope)

        val executionRequest = ExecutionRequest(
            roots, JUnitEngineExecutionListenerAdapter(request.engineExecutionListener, descriptorFactory)
        )

        runtime.execute(executionRequest)
    }
}
