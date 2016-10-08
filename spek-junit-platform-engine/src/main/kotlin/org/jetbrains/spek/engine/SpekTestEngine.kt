package org.jetbrains.spek.engine

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.Dsl
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.SubjectDsl
import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.api.memoized.Subject
import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.jetbrains.spek.engine.memoized.SubjectAdapter
import org.jetbrains.spek.engine.memoized.SubjectImpl
import org.jetbrains.spek.extension.Extension
import org.jetbrains.spek.extension.SpekExtension
import org.junit.platform.commons.util.ReflectionUtils
import org.junit.platform.engine.*
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.engine.discovery.ClasspathSelector
import org.junit.platform.engine.discovery.PackageSelector
import org.junit.platform.engine.discovery.UniqueIdSelector
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import org.junit.platform.engine.support.descriptor.JavaClassSource
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine
import java.util.*
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class SpekTestEngine: HierarchicalTestEngine<SpekExecutionContext>() {
    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val engineDescriptor = SpekEngineDescriptor(uniqueId)
        resolveSpecs(discoveryRequest, engineDescriptor)
        return engineDescriptor
    }

    override fun getId(): String = "spek"

    override fun createExecutionContext(request: ExecutionRequest)
        = SpekExecutionContext(ExtensionRegistryImpl(), request)

    private fun resolveSpecs(discoveryRequest: EngineDiscoveryRequest, engineDescriptor: EngineDescriptor) {
        val isSpec = java.util.function.Predicate<Class<*>> {
            Spek::class.java.isAssignableFrom(it) || SubjectSpek::class.java.isAssignableFrom(it)
        }
        discoveryRequest.getSelectorsByType(ClasspathSelector::class.java).forEach {
            ReflectionUtils.findAllClassesInClasspathRoot(it.classpathRoot, isSpec).forEach {
                resolveSpec(engineDescriptor, it)
            }
        }

        discoveryRequest.getSelectorsByType(PackageSelector::class.java).forEach {
            ReflectionUtils.findAllClassesInPackage(it.packageName, isSpec).forEach {
                resolveSpec(engineDescriptor, it)
            }
        }

        discoveryRequest.getSelectorsByType(ClassSelector::class.java).forEach {
            if (isSpec.test(it.javaClass)) {
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
            if (root.allDescendants.contains(target)) {
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
        val registry = ExtensionRegistryImpl().apply {
            registerExtension(FixturesAdapter())
            registerExtension(SubjectAdapter())
        }

        getSpekExtensions(klass.kotlin)
            .forEach { registry.registerExtension(it) }

        val instance = klass.kotlin.primaryConstructor!!.call()
        val root = Scope.Spec(
            engineDescriptor.uniqueId.append(SPEC_SEGMENT_TYPE, klass.name),
            JavaClassSource(klass), registry, false
        )
        engineDescriptor.addChild(root)

        when(instance) {
            is SubjectSpek<*> -> (instance as SubjectSpek<Any>).spec.invoke(
                SubjectCollector<Any>(root, root.registry)
            )
            is Spek -> instance.spec.invoke(Collector(root, root.registry))
        }

    }

    open class Collector(val root: Scope.Group, val registry: ExtensionRegistryImpl): Dsl {
        override fun group(description: String, pending: Pending, lazy: Boolean, body: Dsl.() -> Unit) {
            val action: Scope.Group.(SpekExecutionContext) -> Unit = if (lazy) {
                {
                    body.invoke(LazyGroupCollector(this, registry, it))
                }
            } else {
                { }
            }

            val group = Scope.Group(
                root.uniqueId.append(GROUP_SEGMENT_TYPE, description), pending, getSource(), lazy, action
            )

            root.addChild(group)

            if (!lazy) {
                body.invoke(Collector(group, registry))
            }
        }

        override fun test(description: String, pending: Pending, body: () -> Unit) {
            val test = Scope.Test(root.uniqueId.append(TEST_SEGMENT_TYPE, description), pending, getSource(), body)
            root.addChild(test)
        }

        override fun beforeEachTest(callback: () -> Unit) {
            registry.getExtension(FixturesAdapter::class)!!.registerBeforeEach(root, callback)
        }

        override fun afterEachTest(callback: () -> Unit) {
            registry.getExtension(FixturesAdapter::class)!!.registerAfterEach(root, callback)
        }
    }

    class LazyGroupCollector(root: Scope.Group, registry: ExtensionRegistryImpl,
                             val context: SpekExecutionContext): Collector(root, registry) {
        override fun group(description: String, pending: Pending, lazy: Boolean, body: Dsl.() -> Unit) {
            fail()
        }

        override fun beforeEachTest(callback: () -> Unit) {
            fail()
        }

        override fun afterEachTest(callback: () -> Unit) {
            fail()
        }

        override fun test(description: String, pending: Pending, body: () -> Unit) {
            val test = Scope.Test(root.uniqueId.append(TEST_SEGMENT_TYPE, description), pending, getSource(), body)
            root.addChild(test)
            context.engineExecutionListener.dynamicTestRegistered(test)
        }

        private inline fun fail() {
            throw SpekException("You're not allowed to do this")
        }
    }

    open class SubjectCollector<T>(root: Scope.Group, registry: ExtensionRegistryImpl)
        : Collector(root, registry), SubjectDsl<T> {
        var _subject: SubjectImpl<T>? = null

        override fun subject(mode: CachingMode, factory: () -> T): Subject<T> {
            return registry.getExtension(SubjectAdapter::class)!!
                .registerSubject(mode, root, factory).apply { _subject = this }
        }

        override val subject: T
            get() {
                if (_subject != null) {
                    return _subject!!.get()
                }
                throw SpekException("Subject not configured.")
            }

        override fun <T, K : SubjectSpek<T>> includeSubjectSpec(spec: KClass<K>) {
            val instance = spec.primaryConstructor!!.call()
            val nestedRegistry = ExtensionRegistryImpl()

            registry.extensions().forEach { nestedRegistry.registerExtension(it) }
            getSpekExtensions(spec)
                .forEach { nestedRegistry.registerExtension(it) }

            val scope = Scope.Spec(
                root.uniqueId.append(SPEC_SEGMENT_TYPE, spec.java.name),
                JavaClassSource(spec.java), nestedRegistry, true
            )
            root.addChild(scope)
            instance.spec.invoke(NestedSubjectCollector(scope, nestedRegistry, this as SubjectCollector<T>))
        }
    }

    class NestedSubjectCollector<T>(root: Scope.Group, registry: ExtensionRegistryImpl, val parent: SubjectCollector<T>)
        : SubjectCollector<T>(root, registry) {
        override fun subject(mode: CachingMode, factory: () -> T): Subject<T> {
            return object: Subject<T> {
                override fun getValue(ref: Any?, property: KProperty<*>): T {
                    return parent.subject
                }
            }
        }

        override val subject: T
            get() = parent.subject
    }

    companion object {
        const val SPEC_SEGMENT_TYPE = "spec";
        const val GROUP_SEGMENT_TYPE = "group";
        const val TEST_SEGMENT_TYPE = "test";

        // TODO: fix me
        fun getSource(): TestSource? = null

        fun getSpekExtensions(spec: KClass<*>): List<Extension> {
            return spec.annotations
                .map {
                    if (it is SpekExtension) {
                        it
                    } else {
                        it.annotationClass.annotations.find {
                            it.annotationClass == SpekExtension::class
                        } as SpekExtension?
                    }

                }
                .filter { it != null }
                .map { it!!.extension.primaryConstructor!!.call() }
        }
    }
}
