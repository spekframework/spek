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
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

actual class SpekRuntime: AbstractRuntime() {
    private val defaultInstanceFactory = object: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val reflections = createReflections(discoveryRequest.sourceDirs)
        val scopes = reflections.getSubTypesOf(Spek::class.java)
            .map(Class<out Spek>::kotlin)
            .filter { it.findAnnotation<Ignore>() == null }
            .filter { !it.isAbstract }
            .map { klass ->
                klass to PathBuilder.from(klass).build()
            }
            .map { (klass, path) ->
                val matched = discoveryRequest.paths.firstOrNull { it.isRelated(path) }
                val root = matched?.let {
                    resolveSpec(instanceFactoryFor(klass).create(klass), path)
                }
                matched to root
            }
            .filter { (path, root) -> path != null && root != null}
            .map { (path, root) ->
                root!!.apply { filterBy(path!!) }
            }
            .filter { !it.isEmpty() }

        return DiscoveryResult(scopes)
    }

    private fun instanceFactoryFor(spek: KClass<*>): InstanceFactory {
        return spek.annotations.filterIsInstance<CreateWith>()
            .map { it.factory }
            .map { it.objectInstance ?: it.primaryConstructor!!.call() }
            .firstOrNull() ?: defaultInstanceFactory
    }

    private fun createReflections(testDirs: List<String>): Reflections {
        val urls = if (testDirs.isEmpty()) {
            ClasspathHelper.forClassLoader()
        } else {
            testDirs.map(::File)
                .map { it.toURI().toURL() }
        }

        return Reflections(
            ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(SubTypesScanner())
        )
    }
}
