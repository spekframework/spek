package org.spek.impl

import org.spek.impl.events.*

public object Runner {
    public fun executeSpec(given : TestGivenAction, listener : Listener) {
        Util.safeExecute(given, listener.given(given.Description())) {
            performInit() forEach { on ->
                Util.safeExecute(on, listener.on(given.Description(), on.Description())) {
                    performInit() forEach { it ->
                        Util.safeExecute(it, listener.it(given.Description(), on.Description(), it.Description())) {
                            run()
                        }
                    }
                }
            }
        }
    }
}
