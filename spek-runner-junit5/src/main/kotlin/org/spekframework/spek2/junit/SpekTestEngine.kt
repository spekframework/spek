package org.spekframework.spek2.junit

import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.*
import org.spekframework.spek2.runtime.JvmDiscoveryContextFactory
import org.spekframework.spek2.runtime.SpekRuntime
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import java.lang.reflect.Modifier
import java.nio.file.Paths
import org.junit.platform.engine.ExecutionRequest as JUnitExecutionRequest

class SpekTestEngine : TestEngine {

    companion object {
        const val ID = "spek2"
        // Spek does not know how to handle these selectors, fallback to no matching tests.
        private val UNSUPPORTED_SELECTORS = listOf(
            MethodSelector::class.java,
            FileSelector::class.java,
            ModuleSelector::class.java,
            ClasspathResourceSelector::class.java,
            UniqueIdSelector::class.java,
            UriSelector::class.java,
            DirectorySelector::class.java
        )
    }

    private val descriptorFactory = SpekTestDescriptorFactory()
    private val runtime by lazy { SpekRuntime() }

    override fun getId() = ID

    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val engineDescriptor = SpekEngineDescriptor(uniqueId, id)

        if (containsUnsupportedSelector(discoveryRequest)) {
            return engineDescriptor
        }

        val sourceDirs = discoveryRequest.getSelectorsByType(ClasspathRootSelector::class.java)
            .map { it.classpathRoot }
            .map { Paths.get(it) }
            .map { it.toString() }

        val classSelectors = discoveryRequest.getSelectorsByType(ClassSelector::class.java)
            .filter {
                // get all super classes
                val superClasses = mutableListOf<String>()
                var current = it.javaClass.superclass
                while (current != null) {
                    superClasses.add(current.name)
                    current = current.superclass
                }

                superClasses.contains("org.spekframework.spek2.Spek")
            }
            .filter {
                !(it.javaClass.isAnonymousClass
                    || it.javaClass.isLocalClass
                    || it.javaClass.isSynthetic
                    || Modifier.isAbstract(it.javaClass.modifiers))
            }.map {
                PathBuilder
                    .from(it.javaClass.kotlin)
                    .build()
            }

        val packageSelectors = discoveryRequest.getSelectorsByType(PackageSelector::class.java)
            .map {
                PathBuilder()
                    .appendPackage(it.packageName)
                    .build()
            }

        val filters = linkedSetOf<Path>()

        filters.addAll(classSelectors)
        filters.addAll(packageSelectors)

        // todo: empty filter should imply root
        if (filters.isEmpty()) {
            filters.add(PathBuilder.ROOT)
        }

        val context = JvmDiscoveryContextFactory.create(sourceDirs)
        val discoveryResult = runtime.discover(DiscoveryRequest(context, filters.toList()))

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

    private fun containsUnsupportedSelector(discoveryRequest: EngineDiscoveryRequest): Boolean {
        for (selector in UNSUPPORTED_SELECTORS) {
            if (discoveryRequest.getSelectorsByType(selector).isNotEmpty()) {
                return true
            }
        }
        return false
    }
}
