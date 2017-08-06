package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.SpekDsl

/**
 * @author Ranie Jade Ramiso
 */
@SpekDsl
interface TestContainer {
    fun test(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit)
}
