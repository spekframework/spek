package org.jetbrains.spek.api.compat

import org.jetbrains.spek.api.dsl.it
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.meta.*

class ActionBody(val scope: GroupBody) {
    @Synonym(SynonymType.TEST, prefix = "it ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun it(description: String, body: TestBody.() -> Unit) {
        scope.it(description, body)
    }
}