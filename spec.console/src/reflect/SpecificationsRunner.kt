package org.spek.console.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.console.listeners.*
import org.spek.console.listeners.text.*
import org.spek.console.output.file.*
import org.spek.console.output.console.*
import org.spek.api.Spek
import org.spek.console.api.CSpek


public class SpecificationRunner(val listener : Listener) {
    fun runSpecs(folder : String, packageName : String) {
        FileClassLoader.getClasses(folder, packageName) forEach { specificationClass ->
            if (!javaClass<CSpek>().isAssignableFrom(specificationClass)) {
                throw RuntimeException("All spec classes should be inherited from ${javaClass<CSpek>()}")
            }

            val spek = (specificationClass.newInstance() as CSpek).allGivens()
            spek forEach {  Runner.executeSpec(it, listener) }
        }
    }
}
