package org.spek.impl

import org.spek.impl.events.*
import org.spek.reflect.DetectedSpek

public object Runner {
    public fun executeSpek(specificationClass: DetectedSpek, listener: Listener) {
        safeExecute(specificationClass, listener.spek(specificationClass.name())) {
            allGiven() forEach { Runner.executeSpec(it, listener) }
        }
    }

    public fun executeSpec(given : TestGivenAction, listener : Listener) {
        safeExecute(given, listener.given(given.description())) {
            performInit() forEach { on ->
                safeExecute(on, listener.on(given.description(), on.description())) {
                    performInit() forEach { it ->
                        safeExecute(it, listener.it(given.description(), on.description(), it.description())) {
                            run()
                        }
                    }
                }
            }
        }
    }
}
