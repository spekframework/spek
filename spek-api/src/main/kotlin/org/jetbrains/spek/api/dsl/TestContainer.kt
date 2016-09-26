package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.meta.SpekDsl

/**
 * @author Ranie Jade Ramiso
 */
@SpekDsl
interface TestContainer {
    fun test(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit)
}
