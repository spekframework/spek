package org.spekframework.spek2.dsl

sealed class Skip {
    class Yes(val reason: String? = null) : Skip()
    object No : Skip()
}
