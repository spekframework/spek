package org.spek.impl

public trait Listener {
    fun given(given : TestGivenAction) : StepListener
    fun on(given : TestGivenAction, on : TestOnAction) : StepListener
    fun it(given : TestGivenAction, on : TestOnAction, it : TestItAction) : StepListener
}

public object Runner {
    public fun executeSpec(given : TestGivenAction, listener : Listener) {
        Util.safeExecute(given, listener.given(given)) {
            performInit() forEach { on ->
                Util.safeExecute(on, listener.on(given, on)) {
                    performInit() forEach { it ->
                        Util.safeExecute(it, listener.it(given, on, it)) {
                            run()
                        }
                    }
                }
            }
        }
    }
}
