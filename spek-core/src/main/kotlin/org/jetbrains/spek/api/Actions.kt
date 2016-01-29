package org.jetbrains.spek.api


interface TestSpekAction {
    fun description(): String
    fun iterateGiven(it:(TestGivenAction) -> Unit)
}

interface TestGivenAction {
    fun description(): String
    fun iterateOn(it: (TestOnAction) -> Unit)
}

interface TestOnAction {
    fun description(): String
    fun iterateIt(it : (TestItAction) -> Unit)
}

interface TestItAction {
    fun description(): String
    fun run()
}

