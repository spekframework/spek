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
    fun runSpecs(packageName : String) {
        FileClassLoader.getClasses(packageName) forEach { specificationClass ->
            if (!javaClass<ConsoleSpek>().isAssignableFrom(specificationClass)) {
                throw RuntimeException("All spec classes should be inherited from ${javaClass<ConsoleSpek>()}")
            }

            val spek = (specificationClass.newInstance() as ConsoleSpek).allGivens()
            spek forEach {  Runner.executeSpec(it, listener) }
        }
    }
}
