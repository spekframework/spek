package org.jetbrains.spek.api


public interface TestSpekAction {
    public fun description(): String
    public fun iterateGiven(it:(TestGivenAction) -> Unit)
}

public interface TestGivenAction {
    public fun description(): String
    public fun iterateOn(it: (TestOnAction) -> Unit)
}

public interface TestOnAction {
    public fun description(): String
    public fun iterateIt(it : (TestItAction) -> Unit)
}

public interface TestItAction {
    public fun description(): String
    public fun run()
}

