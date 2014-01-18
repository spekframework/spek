package org.spek.impl

import org.spek.impl.events.*

public object Runner {
    public fun executeSpek(specificationClass: TestFixtureAction, listener: Listener) {
        val spekDescription = specificationClass.description()
        safeExecute(specificationClass, listener.spek(spekDescription)) {
            iterateGiven { given ->
                val givenDescription = given.description()
                safeExecute(given, listener.given(spekDescription, givenDescription)) {
                    iterateOn { on ->
                        val onDescription = on.description()
                        safeExecute(on, listener.on(spekDescription, givenDescription, onDescription)) {
                            iterateIt { it ->
                                safeExecute(it, listener.it(spekDescription, givenDescription, onDescription, it.description())) {
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
