package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.SpekDsl
import org.spekframework.spek2.meta.Synonym
import org.spekframework.spek2.meta.SynonymType

/**
 * @author Ranie Jade Ramiso
 */
@SpekDsl
interface TestContainer {
    @Synonym(type = SynonymType.Test)
    fun test(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit)
}
