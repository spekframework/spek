package org.spekframework.spek2.junit

import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.engine.discovery.ClasspathRootSelector
import org.junit.platform.engine.discovery.MethodSelector
import org.junit.platform.engine.discovery.PackageSelector
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.Path
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

        val classSelectors = discoveryRequest.getSelectorsByType(ClassSelector::class.java)
            .map {
                val packageName = it.javaClass.`package`.name
                val className = it.javaClass.name.removePrefix("$packageName.")

                PathBuilder()
                    .append(packageName)
                    .append(className)
                    .build()
            }

        val packageSelectors = discoveryRequest.getSelectorsByType(PackageSelector::class.java)
            .map {
                PathBuilder().append(it.packageName)
                    .build()
            }

        val filters = mutableListOf<Path>()

        filters.addAll(classSelectors)
        filters.addAll(packageSelectors)

        // todo: empty filter should imply root
        if (filters.isEmpty()) {
            filters.add(PathBuilder.ROOT)
        }

        val discoveryResult = runtime.discover(DiscoveryRequest(sourceDirs, filters.toList()))

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
