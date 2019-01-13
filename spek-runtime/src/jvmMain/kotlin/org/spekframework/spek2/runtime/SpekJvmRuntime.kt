package org.spekframework.spek2.runtime

import io.github.classgraph.ClassGraph
import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.meta.Ignore
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.isRelated
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.streams.toList

actual class SpekRuntime : AbstractRuntime() {
    private val defaultInstanceFactory = object : InstanceFactory {
        override fun <T : Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    override fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = scanClasses(discoveryRequest.sourceDirs)
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
            .filter { (path, root) -> path != null && root != null }
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

    private fun scanClasses(testDirs: List<String>): List<KClass<out Spek>> {
        val cg = ClassGraph()
            .enableClassInfo()

        if (testDirs.isNotEmpty()) {
            cg.overrideClasspath(testDirs)
        }

        return cg.scan().use {
            it.getSubclasses(Spek::class.qualifiedName!!).stream()
                .map { it.loadClass() as Class<out Spek> }
                .map { it.kotlin }
                .toList()
        }
    }
}
