package org.jetbrains.spek.api


public trait TestSpekAction {
    public fun description(): String
    public fun iterateGiven(it:(TestGivenAction) -> Unit)
}

public trait TestGivenAction {
    public fun description(): String
    public fun iterateOn(it: (TestOnAction) -> Unit)
}

public trait TestOnAction {
    public fun description(): String
    public fun iterateIt(it : (TestItAction) -> Unit)
}

public trait TestItAction {
    public fun description(): String
    public fun run()
}

