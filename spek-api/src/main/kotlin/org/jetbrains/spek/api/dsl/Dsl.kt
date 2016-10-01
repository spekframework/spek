package org.jetbrains.spek.api.dsl

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface Dsl {
    fun group(description: String, pending: Pending = Pending.No, lazy: Boolean = false, body: Dsl.() -> Unit)
    fun test(description: String, pending: Pending = Pending.No, body: () -> Unit)

    fun beforeEachTest(callback: () -> Unit)
    fun afterEachTest(callback: () -> Unit)
    // fun <T: Spek> includeSpec(spec: KClass<T>)
}
