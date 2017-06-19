package org.spekframework.jvm

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.lifecycle.InstanceFactory
import org.spekframework.runtime.SpekRuntime
import kotlin.reflect.KClass

class SpekJvmRuntime: SpekRuntime() {
    override val defaultInstanceFactory: InstanceFactory = object: InstanceFactory {
        override fun <T: Spek> create(spek: KClass<T>): T {
            return spek.objectInstance ?: spek.constructors.first { it.parameters.isEmpty() }
                .call()
        }
    }

    override fun listSpecsForPackage(`package`: String): List<KClass<out Spek>> {
        TODO()
    }
}
