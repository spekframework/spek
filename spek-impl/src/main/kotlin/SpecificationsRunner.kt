package org.spek.console.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.api.annotations.skip

public class SpecificationRunner(val listener: Listener) {
    public fun runSpecs(packageName: String) {
        val classes = FileClassLoader.findTestsInPackage(packageName)
        doTestRun(classes)
    }

    public fun runSpecs(clazz: Class<*>) {
        val classes = FileClassLoader.findTestsInClass(clazz)
        doTestRun(classes)
    }

    private fun doTestRun(classes : List<DetectedSpek>) {
        classes forEach { specificationClass ->
            Util.safeExecute( specificationClass, listener.spek(specificationClass.name())) {
                allGiven() forEach { Runner.executeSpec(it, listener) }
            }
        }
    }
}
