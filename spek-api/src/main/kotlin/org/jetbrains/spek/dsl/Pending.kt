package org.jetbrains.spek.dsl

/**
 * @author Ranie Jade Ramiso
 */
sealed class Pending private constructor(val pending: Boolean) {
    class Yes(val reason: String? = null): Pending(true)
    object No: Pending(false)
}
