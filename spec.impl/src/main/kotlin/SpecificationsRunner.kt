package org.spek.console.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.api.skip

public class SpecificationRunner(val listener: Listener) {
    fun runSpecs(packageName: String) {
        val classes = FileClassLoader.getClasses(packageName)

        classes forEach { specificationClass ->
            Util.safeExecute( specificationClass, listener.spek(specificationClass.name())) {
                allGiven() forEach { Runner.executeSpec(it, listener) }
            }
        }
    }
}
