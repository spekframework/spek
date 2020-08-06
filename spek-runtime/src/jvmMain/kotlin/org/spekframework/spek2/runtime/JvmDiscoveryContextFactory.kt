package org.spekframework.spek2.runtime

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import org.spekframework.spek2.CreateWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import org.spekframework.spek2.meta.Ignore
import org.spekframework.spek2.runtime.discovery.DiscoveryContext
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

object JvmDiscoveryContextFactory {
    // classgraph scan is already done in parallel, this property allows it to be overriden
    private val classpathScanConcurrency = System.getProperty("spek2.jvm.cg.scan.concurrency")?.toInt()

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
            // any jar on the classpath won't be scanned
            .disableJarScanning()

        if (testDirs.isNotEmpty()) {
            cg.overrideClasspath(System.getProperty("java.class.path"), *testDirs.toTypedArray())
        }

        val scanResult = if (classpathScanConcurrency != null) {
            cg.scan(classpathScanConcurrency)
        } else {
            cg.scan()
        }

        return filterScanResult(scanResult)
    }

    private fun filterScanResult(scanResult: ScanResult) : List<KClass<out Spek>> {
        return scanResult.getSubclasses(Spek::class.qualifiedName!!)
            .map { it.loadClass() as Class<out Spek> }
            .filter { !it.isAnonymousClass }
            .map { it.kotlin }
            .toList()
    }
}
