package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.*

@SpekDsl
interface TestContainer {
    @Synonym(type = SynonymType.TEST)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun test(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit)

    /**
     * Creates a [test][GroupBody.test].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.TEST, prefix = "it")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun it(description: String, body: TestBody.() -> Unit) {
        test("it $description", body = body)
    }

    /**
     * Creates a pending [test][GroupBody.test].
     *
     * @author Ranie Jade Ramiso
     * @since 1.0
     */
    @Synonym(type = SynonymType.TEST, prefix = "it", excluded = true)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun xit(description: String, reason: String? = null, body: TestBody.() -> Unit = {}) {
        test("it $description", Pending.Yes(reason), body)
    }
}
