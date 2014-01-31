package org.spek.impl;

import org.spek.reflect.*
import org.spek.api.*

public class SpecificationRunner(val listener: Listener) {
    public fun runSpecs(packageName: String) {
        doTestRun(FileClassLoader.findTestsInPackage(packageName))
    }

    public fun runSpecs(clazz: Class<*>) {
        doTestRun(FileClassLoader.findTestsInClass(clazz))
    }

    private fun doTestRun(classes : List<TestSpekAction>) {
        classes forEach {
            executeSpek(it, listener)
        }
    }
}

public fun executeSpek(specificationClass: TestSpekAction, listener: Listener) {
    val spekDescription = specificationClass.description()
    executeWithReporting(specificationClass, listener.spek(spekDescription)) {
        iterateGiven { given ->
            val givenDescription = given.description()
            executeWithReporting(given, listener.given(spekDescription, givenDescription)) {
                iterateOn { on ->
                    val onDescription = on.description()
                    executeWithReporting(on, listener.on(spekDescription, givenDescription, onDescription)) {
                        iterateIt { it ->
                            executeWithReporting(it, listener.it(spekDescription, givenDescription, onDescription, it.description())) {
                                run()
                            }
                        }
                    }
                }
            }
        }
    }
}
