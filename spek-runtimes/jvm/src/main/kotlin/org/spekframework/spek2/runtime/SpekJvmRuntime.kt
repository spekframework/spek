package org.spekframework.spek2.runtime

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Ignore
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.isRelated
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

// instantiate only once, it's very expensive
val reflections = Reflections(
    ConfigurationBuilder()
        .setUrls(ClasspathHelper.forClassLoader())
        .setScanners(SubTypesScanner())
)

actual class SpekRuntime: AbstractRuntime() {
    private val defaultInstanceFactory = object: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = reflections.getSubTypesOf(Spek::class.java)
            .map(Class<out Spek>::kotlin)
            .filter { it.findAnnotation<Ignore>() == null }
            .filter { !it.isAbstract }
            .map { klass ->
                klass to PathBuilder.from(klass).build()
            }
            .filter { (_, path) ->
                discoveryRequest.path.isRelated(path)
            }
            .map { (klass, path) ->
                resolveSpec(instanceFactoryFor(klass).create(klass), path)
            }
            // TODO: should we move final filtering to SpekRuntime?
            .map { it.apply { filterBy(discoveryRequest.path) } }
            .filter { !it.isEmpty() }

        return DiscoveryResult(scopes)
    }

    private fun instanceFactoryFor(spek: KClass<*>): InstanceFactory {
        return spek.annotations.filterIsInstance<CreateWith>()
            .map { it.factory }
            .map { it.objectInstance ?: it.primaryConstructor!!.call() }
            .firstOrNull() ?: defaultInstanceFactory
    }
}
