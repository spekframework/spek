package org.jetbrains.spek.api.dsl

/**
 * @author Ranie Jade Ramiso
 */
sealed class Pending private constructor(val pending: Boolean) {
    class Yes(val reason: String? = null): org.jetbrains.spek.api.Pending(true)
    object No: org.jetbrains.spek.api.Pending(false)
}
