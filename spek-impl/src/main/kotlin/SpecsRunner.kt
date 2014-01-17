package org.spek.impl

import org.spek.impl.events.*

public object Runner {
    public fun executeSpek(specificationClass: TestFixtureAction, listener: Listener) {
        safeExecute(specificationClass, listener.spek(specificationClass.description())) {
            allGiven() forEach { given ->
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
    }
}
