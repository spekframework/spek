package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.ActionBody
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.meta.*

@SpekDsl
class Feature(delegate: GroupBody) : GroupBody by delegate {
    @Synonym(SynonymType.ACTION, prefix = "Scenario: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Scenario(description: String, skip: Skip = Skip.No, body: Scenario.() -> Unit) {
        action("Scenario: $description", skip) {
            body(Scenario(this))
        }
    }
}

// Give, When, Then and And don't need synonym annotations since
// they should not be executed individually.
@SpekDsl
class Scenario(delegate: ActionBody) : ActionBody by delegate {
    fun Given(description: String, body: TestBody.() -> Unit) {
        test("Given $description", body = body)
    }

    fun When(description: String, body: TestBody.() -> Unit) {
        test("When $description", body = body)
    }

    fun Then(description: String, body: TestBody.() -> Unit) {
        test("Then $description", body = body)
    }

    fun And(description: String, body: TestBody.() -> Unit) {
        test("Then $description", body = body)
    }
}

@Synonym(SynonymType.GROUP, prefix = "Feature: ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.Feature(description: String, skip: Skip = Skip.No, body: Feature.() -> Unit) {
    group("Feature: $description", skip) {
        body(Feature(this))
    }
}
