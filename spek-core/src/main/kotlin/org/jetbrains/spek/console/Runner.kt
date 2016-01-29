package org.jetbrains.spek.console

import org.jetbrains.spek.api.TestSpekAction


public class Runner(val listener: WorkflowReporter) {
    public fun runSpecs(paths: List<String>, packageName: String) {
        run(findSpecs(paths, packageName))
    }

    private fun run(classes: List<TestSpekAction>) {
        classes.forEach {
            executeSpek(it, listener)
        }
    }
}

public fun executeSpek(specificationClass: TestSpekAction, listener: WorkflowReporter) {
    val spekDescription = specificationClass.description()
    executeWithReporting(specificationClass, listener.spek(spekDescription)) {
        listGiven().forEach { given ->
            val givenDescription = given.description()
            executeWithReporting(given, listener.given(spekDescription, givenDescription)) {
                listOn().forEach { on ->
                    given.run { // calls beforeEach/afterEach
                        on.run { // calls beforeOn/afterOn
                            val onDescription = on.description()
                            executeWithReporting(on, listener.on(spekDescription, givenDescription, onDescription)) {
                                listIt().forEach { it ->
                                    executeWithReporting(it, listener.it(spekDescription, givenDescription, onDescription, it.description())) {
                                        run {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
