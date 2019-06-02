package org.spekframework.spek2.runtime

import io.github.classgraph.ClassGraph
import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.meta.Ignore
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.streams.toList

object JvmDiscoveryContextFactory {
    private val defaultInstanceFactory = object : InstanceFactory {
        override fun <T : Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    fun create(testDirs: List<String>): DiscoveryContext {
        val classes = scanClasses(testDirs)
        val builder = DiscoveryContext.builder()
        classes.filter { !it.isAbstract }
            .filter { it.findAnnotation<Ignore>() == null }
            .map { cls ->
                val instanceFactory = instanceFactoryFor(cls)
                cls to { instanceFactory.create(cls) }
            }.forEach { (cls, factory) ->
                builder.addTest(cls, factory)
            }

        return builder.build()
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
            cg.overrideClasspath(System.getProperty("java.class.path"), testDirs)
        }

        return cg.scan().use {
            it.getSubclasses(Spek::class.qualifiedName!!).stream()
                .map { it.loadClass() as Class<out Spek> }
                .filter { !it.isAnonymousClass }
                .map { it.kotlin }
                .toList()
        }
    }
}
