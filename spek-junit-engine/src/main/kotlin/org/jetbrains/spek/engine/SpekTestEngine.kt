package org.jetbrains.spek.engine

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.Dsl
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.dsl.SubjectDsl
import org.jetbrains.spek.api.subject.Subject
import org.jetbrains.spek.engine.scope.Scope
import org.jetbrains.spek.engine.subject.SubjectImpl
import org.junit.gen5.commons.util.ReflectionUtils
import org.junit.gen5.engine.EngineDiscoveryRequest
import org.junit.gen5.engine.ExecutionRequest
import org.junit.gen5.engine.TestDescriptor
import org.junit.gen5.engine.UniqueId
import org.junit.gen5.engine.discovery.ClassSelector
import org.junit.gen5.engine.discovery.ClasspathSelector
import org.junit.gen5.engine.discovery.PackageSelector
import org.junit.gen5.engine.support.descriptor.EngineDescriptor
import org.junit.gen5.engine.support.hierarchical.HierarchicalTestEngine
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
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

    override fun createExecutionContext(request: ExecutionRequest) = SpekExecutionContext()

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
        val instance = klass.kotlin.primaryConstructor!!.call()
        val name = when(instance) {
            is SubjectSpek<*> -> (klass.genericSuperclass as ParameterizedType).actualTypeArguments.first().typeName
            else -> klass.name
        }
        val root = Scope.Group(engineDescriptor.uniqueId.append(SPEC_SEGMENT_TYPE, name), Pending.No)
        engineDescriptor.addChild(root)

        when(instance) {
            is SubjectSpek<*> -> (instance as SubjectSpek<Any>).spec.invoke(SubjectCollector<Any>(root))
            is Spek -> instance.spec.invoke(Collector(root))
        }

    }

    open class Collector(val root: Scope.Group): Dsl {
        override fun group(description: String, pending: Pending, body: Dsl.() -> Unit) {
            val group = Scope.Group(root.uniqueId.append(GROUP_SEGMENT_TYPE, description), pending)
            root.addChild(group)
            body.invoke(Collector(group))
        }

        override fun test(description: String, pending: Pending, body: () -> Unit) {
            val test = Scope.Test(root.uniqueId.append(TEST_SEGMENT_TYPE, description), pending, body)
            root.addChild(test)
        }

        override fun beforeEach(callback: () -> Unit) {
            root.fixtures.beforeEach.add(callback)
        }

        override fun afterEach(callback: () -> Unit) {
            root.fixtures.afterEach.add(callback)
        }
    }

    class SubjectCollector<T>(root: Scope.Group): Collector(root), SubjectDsl<T> {
        override fun subject(factory: () -> T): Subject<T> {
            return SubjectImpl(factory).apply {
                root.subject = this
            }
        }

        override val subject: T
            get() {
                if (root.subject != null) {
                    return root.subject!!.get() as T
                }
                throw SpekException("Subject not configured.")
            }

        override fun <T, K : SubjectSpek<T>> includeSubjectSpec(spec: KClass<K>) {
            TODO()
        }

    }

    companion object {
        const val SPEC_SEGMENT_TYPE = "spec";
        const val GROUP_SEGMENT_TYPE = "group";
        const val TEST_SEGMENT_TYPE = "test";
    }
}
