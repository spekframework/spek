package org.spek.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.reflect.*
import org.spek.api.*

public class SpecificationRunner(val listener: Listener) {
    public fun runSpecs(packageName: String) {
        doTestRun(FileClassLoader.findTestsInPackage(packageName))
    }

    public fun runSpecs(clazz: Class<*>) {
        doTestRun(FileClassLoader.findTestsInClass(clazz))
    }

    private fun doTestRun(classes : List<TestFixtureAction>) {
        classes forEach {
            Runner.executeSpek(it, listener)
        }
    }
}
