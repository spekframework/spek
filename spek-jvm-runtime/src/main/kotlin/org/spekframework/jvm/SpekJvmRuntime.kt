package org.spekframework.jvm

import org.jetbrains.spek.api.CreateWith
import org.jetbrains.spek.api.IgnoreSpek
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.lifecycle.InstanceFactory
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.spekframework.runtime.SpekRuntime
import org.spekframework.runtime.execution.DiscoveryRequest
import org.spekframework.runtime.execution.DiscoveryResult
import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.scope.isRelated
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

fun classToPath(spek: KClass<out Spek>): Path {
    val packagePath = JvmPath.create(spek.java.`package`.name, JvmPath.ROOT)
    val classPath = JvmPath.create(spek.java.simpleName!!, packagePath)
    return classPath
}

// instantiate only once, it's very expensive
val reflections = Reflections(
    ConfigurationBuilder()
        .setUrls(ClasspathHelper.forClassLoader())
        .setScanners(SubTypesScanner())
)

class SpekJvmRuntime: SpekRuntime() {
    val defaultInstanceFactory = object: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = reflections.getSubTypesOf(Spek::class.java)
            .map(Class<out Spek>::kotlin)
            .filter { it.findAnnotation<IgnoreSpek>() == null }
            .filter { !it.isAbstract }
            .filter { discoveryRequest.path.isRelated(classToPath(it)) }
            .map { resolveSpec(it, classToPath(it)) }
            // TODO: should we move final filtering to SpekRuntime?
            .map { it.apply { filterBy(discoveryRequest.path) } }
            .filter { !it.isEmpty() }

        return DiscoveryResult(scopes)
    }

    override fun instanceFactoryFor(spek: KClass<*>): InstanceFactory {
        val factory = spek.annotations.filterIsInstance<CreateWith>()
            .map { it.factory }
            .map { it.objectInstance ?: it.primaryConstructor!!.call() }
            .firstOrNull() ?: defaultInstanceFactory
        return factory
    }
}
