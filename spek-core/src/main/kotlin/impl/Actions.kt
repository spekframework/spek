package org.spek.impl

import org.spek.*

trait TestSpekAction {
    fun description(): String
    fun iterateGiven(it:(TestGivenAction) -> Unit)
}

trait TestGivenAction {
    fun description(): String
    fun iterateOn(it: (TestOnAction) -> Unit)
}

trait TestOnAction {
    fun description(): String
    fun iterateIt(it : (TestItAction) -> Unit)
}

trait TestItAction {
    fun description(): String
    fun run()
}

