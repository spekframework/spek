package org.spekframework.spek2.dsl

/**
 * @since 1.0
 */
sealed class Pending constructor(val pending: Boolean) {
    class Yes(val reason: String? = null): Pending(true)
    object No: Pending(false)
}
