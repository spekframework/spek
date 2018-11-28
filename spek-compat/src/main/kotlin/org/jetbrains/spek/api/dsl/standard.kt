package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.compat.ActionBody
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.dsl.TestContainer
import org.spekframework.spek2.meta.*


@Synonym(SynonymType.GROUP, prefix = "describe ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.describe(description: String, body: GroupBody.() -> Unit) {
    group("describe $description", body = body)
}

@Synonym(SynonymType.GROUP, prefix = "context ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.context(description: String, body: GroupBody.() -> Unit) {
    group("context $description", body = body)
}

@Synonym(SynonymType.GROUP, prefix = "given ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.given(description: String, body: GroupBody.() -> Unit) {
    group("given $description", body = body)
}

@Synonym(SynonymType.GROUP, prefix = "on ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.on(description: String, body: ActionBody.() -> Unit) {
    group("on $description") {
        beforeGroup { body(ActionBody(this)) }
    }
}

@Synonym(SynonymType.TEST, prefix = "it ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun TestContainer.it(description: String, body: TestBody.() -> Unit) {
    test("it $description", body = body)
}

@Synonym(SynonymType.GROUP, prefix = "describe ", excluded = true)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.xdescribe(description: String, reason: String? = null, body: GroupBody.() -> Unit) {
    group("describe $description", Skip.Yes(reason), body = body)
}

@Synonym(SynonymType.GROUP, prefix = "context ", excluded = true)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.xcontext(description: String, reason: String? = null, body: GroupBody.() -> Unit) {
    group("context $description", Skip.Yes(reason), body = body)
}

@Synonym(SynonymType.GROUP, prefix = "given ", excluded = true)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.xgiven(description: String, reason: String? = null, body: GroupBody.() -> Unit) {
    group("given $description", Skip.Yes(reason), body = body)
}

@Synonym(SynonymType.GROUP, prefix = "on ", excluded = true)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
    group("on $description", Skip.Yes(reason)) {
        beforeGroup { body(ActionBody(this)) }
    }
}

@Synonym(SynonymType.TEST, prefix = "it ", excluded = true)
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun TestContainer.xit(description: String, reason: String? = null, body: TestBody.() -> Unit = {}) {
    test("it $description", Skip.Yes(reason), body)
}