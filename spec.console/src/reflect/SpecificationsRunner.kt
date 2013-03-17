package org.spek.console.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.console.listeners.*
import org.spek.console.listeners.text.*
import org.spek.console.output.file.*
import org.spek.console.output.console.*
import org.spek.api.Spek
import org.spek.console.api.ConsoleSpek


public class SpecificationRunner(val listener : Listener) {
    private val BASE_CLASS = javaClass<ConsoleSpek>()
    fun runSpecs(packageName : String) {
        val classes = FileClassLoader.getClasses(BASE_CLASS, packageName)

        classes forEach { specificationClass ->
            if (!BASE_CLASS.isAssignableFrom(specificationClass)) {
                throw RuntimeException("All spec classes should be inherited from $BASE_CLASS")
            }

            val spek = (specificationClass.newInstance() as ConsoleSpek).allGivens()
            spek forEach {  Runner.executeSpec(it, listener) }
        }
    }
}
