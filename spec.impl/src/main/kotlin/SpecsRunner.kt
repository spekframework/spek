package org.spek.impl

import org.spek.impl.events.*

public object Runner {
    public fun executeSpec(given : TestGivenAction, listener : Listener) {
        Util.safeExecute(given, listener.given(given.description())) {
            performInit() forEach { on ->
                Util.safeExecute(on, listener.on(given.description(), on.description())) {
                    performInit() forEach { it ->
                        Util.safeExecute(it, listener.it(given.description(), on.description(), it.description())) {
                            run()
                        }
                    }
                }
            }
        }
    }
}
