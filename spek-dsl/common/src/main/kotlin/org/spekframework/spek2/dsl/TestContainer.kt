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

    /**
     * Creates a [test][SpecBody.test].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Test, prefix = "it")
    fun it(description: String, body: TestBody.() -> Unit) {
        test("it $description", body = body)
    }

    /**
     * Creates a pending [test][SpecBody.test].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.Test, prefix = "it", excluded = true)
    fun xit(description: String, reason: String? = null, body: TestBody.() -> Unit = {}) {
        test("it $description", Pending.Yes(reason), body)
    }
}
