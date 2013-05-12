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
            Util.safeExecute( specificationClass, listener.spek(specificationClass.name())) {
                allGiven() forEach { Runner.executeSpec(it, listener) }
            }
        }
    }
}
