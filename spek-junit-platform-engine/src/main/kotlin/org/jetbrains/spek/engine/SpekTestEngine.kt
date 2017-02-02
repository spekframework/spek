package org.jetbrains.spek.engine

import org.jetbrains.spek.api.CreateWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.ActionBody
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.InstanceFactory
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.api.lifecycle.LifecycleListener
import org.jetbrains.spek.engine.lifecycle.LifecycleAwareAdapter
import org.jetbrains.spek.engine.lifecycle.LifecycleManager
import org.junit.platform.commons.util.ReflectionUtils
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.engine.discovery.ClasspathRootSelector
import org.junit.platform.engine.discovery.PackageSelector
import org.junit.platform.engine.discovery.UniqueIdSelector
import org.junit.platform.engine.support.descriptor.ClassSource
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine
import java.lang.reflect.Modifier
import java.nio.file.Paths
import java.util.LinkedList
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class SpekTestEngine: HierarchicalTestEngine<SpekExecutionContext>() {

    val defaultInstanceFactory = object: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val engineDescriptor = SpekEngineDescriptor(uniqueId)
        resolveSpecs(discoveryRequest, engineDescriptor)
        return engineDescriptor
    }

    override fun getId(): String = "spek"

    override fun createExecutionContext(request: ExecutionRequest)
        = SpekExecutionContext(request)

    private fun resolveSpecs(discoveryRequest: EngineDiscoveryRequest, engineDescriptor: EngineDescriptor) {
        val isValidSpec = java.util.function.Predicate<Class<*>> {
            Spek::class.java.isAssignableFrom(it) && !Modifier.isAbstract(it.modifiers)
        }

        val isSpecClass = java.util.function.Predicate<String>(String::isNotEmpty)
        discoveryRequest.getSelectorsByType(ClasspathRootSelector::class.java).forEach {
            ReflectionUtils.findAllClassesInClasspathRoot(Paths.get(it.classpathRoot), isValidSpec, isSpecClass)
                .forEach {
                    resolveSpec(engineDescriptor, it)
                }
        }

        discoveryRequest.getSelectorsByType(PackageSelector::class.java).forEach {
            ReflectionUtils.findAllClassesInPackage(it.packageName, isValidSpec, isSpecClass).forEach {
                resolveSpec(engineDescriptor, it)
            }
        }

        discoveryRequest.getSelectorsByType(ClassSelector::class.java).forEach {
            if (isValidSpec.test(it.javaClass)) {
                resolveSpec(engineDescriptor, it.javaClass as Class<Spek>)
            }
        }

        discoveryRequest.getSelectorsByType(UniqueIdSelector::class.java).forEach {
            engineDescriptor.findByUniqueId(it.uniqueId).ifPresent(Consumer {
                filterOutUniqueId(it, engineDescriptor)
            })
        }
    }

    private fun filterOutUniqueId(target: TestDescriptor, root: TestDescriptor) {
        if (target != root) {
            if (root.descendants.contains(target)) {
                val descriptors = LinkedList<TestDescriptor>()
                root.children.forEach {
                    descriptors.add(it)
                }

                descriptors.forEach { filterOutUniqueId(target, it) }
            } else {
                root.removeFromHierarchy()
            }
        }
    }

    private fun resolveSpec(engineDescriptor: EngineDescriptor, klass: Class<*>) {
        val fixtures = FixturesAdapter()
        val lifecycleManager = LifecycleManager().apply {
            addListener(fixtures)
        }

        val kotlinClass = klass.kotlin
        val instance = instanceFactoryFor(kotlinClass).create(kotlinClass as KClass<Spek>)
        val root = Scope.Group(
            engineDescriptor.uniqueId.append(SPEC_SEGMENT_TYPE, klass.name),
            Pending.No,
            ClassSource(klass), lifecycleManager
        )
        engineDescriptor.addChild(root)

        instance.spec.invoke(Collector(root, lifecycleManager, fixtures))

    }

    private fun instanceFactoryFor(spek: KClass<*>): InstanceFactory {
        val factory = spek.annotations.filterIsInstance<CreateWith>()
            .map { it.factory }
            .map { it.objectInstance ?: it.primaryConstructor!!.call() }
            .firstOrNull() ?: defaultInstanceFactory
        return factory
    }

    open class Collector(val root: Scope.Group,
                         val lifecycleManager: LifecycleManager,
                         val fixtures: FixturesAdapter): Spec {

        override fun <T> memoized(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
            return LifecycleAwareAdapter(mode, factory).apply {
                registerListener(this)
            }
        }

        override fun registerListener(listener: LifecycleListener) {
            lifecycleManager.addListener(listener)
        }

        override fun group(description: String, pending: Pending, body: SpecBody.() -> Unit) {
            val group = Scope.Group(
                root.uniqueId.append(GROUP_SEGMENT_TYPE, description),
                pending, getSource(), lifecycleManager
            )
            try {
                body.invoke(Collector(group, lifecycleManager, fixtures))
                root.addChild(group)
            } catch(t: Throwable) {
                group(description: String, pending, body = {
                    it("unexpected exception occurred during set up!!!") {
                        throw t
                    }
                })
            }

        }

        override fun action(description: String, pending: Pending, body: ActionBody.() -> Unit) {
            val action = Scope.Action(
                root.uniqueId.append(GROUP_SEGMENT_TYPE, description),
                pending, getSource(), lifecycleManager, {
                    body.invoke(ActionCollector(this, lifecycleManager, it))
                }
            )

            root.addChild(action)
        }

        override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
            val test = Scope.Test(
                root.uniqueId.append(TEST_SEGMENT_TYPE, description),
                pending, getSource(), lifecycleManager, body
            )
            root.addChild(test)
        }

        override fun beforeEachTest(callback: () -> Unit) {
            fixtures.registerBeforeEachTest(root, callback)
        }

        override fun afterEachTest(callback: () -> Unit) {
            fixtures.registerAfterEachTest(root, callback)
        }

        override fun beforeGroup(callback: () -> Unit) {
            fixtures.registerBeforeGroup(root, callback)
        }

        override fun afterGroup(callback: () -> Unit) {
            fixtures.registerAfterGroup(root, callback)
        }
    }

    class ActionCollector(val root: Scope.Action, val lifecycleManager: LifecycleManager,
                          val context: SpekExecutionContext): ActionBody {

        override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
            val test = Scope.Test(
                root.uniqueId.append(TEST_SEGMENT_TYPE, description), pending, getSource(), lifecycleManager, body
            )
            root.addChild(test)
            context.engineExecutionListener.dynamicTestRegistered(test)
        }

    }

    companion object {
        const val SPEC_SEGMENT_TYPE = "spec"
        const val GROUP_SEGMENT_TYPE = "group"
        const val TEST_SEGMENT_TYPE = "test"

        // TODO: fix me
        fun getSource(): TestSource? = null
    }
}
