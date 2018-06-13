package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.*

@SpekDsl
interface TestContainer {
    @Synonym(type = SynonymType.TEST)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun test(description: String, skip: Skip = Skip.No, body: TestBody.() -> Unit)
}
