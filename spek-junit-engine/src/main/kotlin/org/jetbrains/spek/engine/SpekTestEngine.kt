package org.jetbrains.spek.engine

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.Dsl
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.SubjectDsl
import org.jetbrains.spek.extension.Extension
import org.jetbrains.spek.extension.SpekExtension
import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.api.memoized.Subject
import org.jetbrains.spek.engine.extension.ExtensionRegistryImpl
import org.jetbrains.spek.engine.memoized.SubjectAdapter
import org.jetbrains.spek.engine.memoized.SubjectImpl
import org.junit.platform.commons.util.ReflectionUtils
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.engine.discovery.ClasspathSelector
import org.junit.platform.engine.discovery.PackageSelector
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine
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

    override fun createExecutionContext(request: ExecutionRequest) = SpekExecutionContext(ExtensionRegistryImpl())

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
    }

    private fun resolveSpec(engineDescriptor: EngineDescriptor, klass: Class<*>) {
        val registry = ExtensionRegistryImpl().apply {
            registerExtension(FixturesAdapter())
            registerExtension(SubjectAdapter())
        }

        getSpekExtensions(klass.kotlin)
            .forEach { registry.registerExtension(it) }

        val instance = klass.kotlin.primaryConstructor!!.call()
        val root = Scope.Spec(engineDescriptor.uniqueId.append(SPEC_SEGMENT_TYPE, klass.name), registry)
        engineDescriptor.addChild(root)

        when(instance) {
            is SubjectSpek<*> -> (instance as SubjectSpek<Any>).spec.invoke(
                SubjectCollector<Any>(root, root.registry)
            )
            is Spek -> instance.spec.invoke(Collector(root, root.registry))
        }

    }

    open class Collector(val root: Scope.Group, val registry: ExtensionRegistryImpl): Dsl {
        override fun group(description: String, pending: Pending, body: Dsl.() -> Unit) {
            val group = Scope.Group(root.uniqueId.append(GROUP_SEGMENT_TYPE, description), pending)
            root.addChild(group)
            body.invoke(Collector(group, registry))
        }

        override fun test(description: String, pending: Pending, body: () -> Unit) {
            val test = Scope.Test(root.uniqueId.append(TEST_SEGMENT_TYPE, description), pending, body)
            root.addChild(test)
        }

        override fun beforeEach(callback: () -> Unit) {
            registry.getExtension(FixturesAdapter::class)!!.registerBeforeEach(root, callback)
        }

        override fun afterEach(callback: () -> Unit) {
            registry.getExtension(FixturesAdapter::class)!!.registerAfterEach(root, callback)
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

            val scope = Scope.Spec(root.uniqueId.append(SPEC_SEGMENT_TYPE, spec.java.name), nestedRegistry)
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


        fun getSpekExtensions(spec: KClass<*>): List<out Extension> {
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
