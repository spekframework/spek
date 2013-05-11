package org.spek.console.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.console.api.ConsoleSpek
import org.spek.api.skip

public class SpecificationRunner(val listener: Listener) {
    private val BASE_CLASS = javaClass<ConsoleSpek>()
    fun runSpecs(packageName: String) {
        val classes = FileClassLoader.getClasses(BASE_CLASS, packageName)

        classes forEach { specificationClass ->
            Util.safeExecute( specificationClass, object : StepListener {
                override fun executionSkipped(why: String) {
                    listener.spek(specificationClass.name()).executionSkipped(why)
                }
                override fun executionPending(why: String) {
                    super<StepListener>.executionPending(why)
                }
                override fun executionFailed(error: Throwable) {
                    super<StepListener>.executionFailed(error)
                }
            }) {
                allGiven() forEach { Runner.executeSpec(it, listener) }
            }
        }
    }
}
