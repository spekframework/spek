package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.meta.*

@SpekDsl
class GivenBody(delegate: GroupBody) : GroupBody by delegate {
    @Synonym(SynonymType.GROUP, prefix = "When: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun When(description: String, skip: Skip = Skip.No, body: WhenBody.() -> Unit) {
        group("When: $description", skip) {
            body(WhenBody(this))
        }
    }
}

@SpekDsl
class WhenBody(delegate: GroupBody) : GroupBody by delegate {
    @Synonym(SynonymType.TEST, prefix = "Then: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Then(description: String, skip: Skip = Skip.No, body: ThenBody.() -> Unit) {
        test("Then: $description", skip) {
            body(ThenBody(this))
        }
    }
}


@SpekDsl
class ThenBody(delegate: TestBody) : TestBody by delegate

@Synonym(SynonymType.GROUP, prefix = "Given: ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.Given(description: String, skip: Skip = Skip.No, body: GivenBody.() -> Unit) {
    group("Feature: $description", skip) {
        body(GivenBody(this))
    }
}
