package org.jetbrains.spek.api.dsl

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface Dsl {
    fun group(description: String, pending: Pending = Pending.No, body: org.jetbrains.spek.api.dsl.Dsl.() -> Unit)
    fun test(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit)

    fun beforeEach(callback: TestBody.() -> Unit)
    fun afterEach(callback: () -> Unit)

    // fun <T: Spek> includeSpec(spec: KClass<T>)
}
