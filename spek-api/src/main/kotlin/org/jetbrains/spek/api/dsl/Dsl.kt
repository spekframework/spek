package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.annotation.Beta
import org.jetbrains.spek.api.extension.ExtensionRegistry

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
interface Dsl {
    fun group(description: String, pending: Pending = Pending.No, body: Dsl.() -> Unit)
    fun test(description: String, pending: Pending = Pending.No, body: () -> Unit)

    fun beforeEach(callback: () -> Unit)
    fun afterEach(callback: () -> Unit)

    @Beta
    val registry: ExtensionRegistry
    // fun <T: Spek> includeSpec(spec: KClass<T>)
}
