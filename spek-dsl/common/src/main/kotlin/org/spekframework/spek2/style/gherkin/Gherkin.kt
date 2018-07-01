package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.meta.*

@Synonym(SynonymType.GROUP, prefix = "Feature: ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.Feature(description: String, body: FeatureBody.() -> Unit) {
    group("Feature: $description", defaultCachingMode = CachingMode.GROUP) {
        body(FeatureBody(this))
    }
}

@SpekDsl
class FeatureBody(delegate: GroupBody): GroupBody by delegate {
    @Synonym(SynonymType.GROUP, prefix = "Scenario: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Scenario(description: String, body: ScenarioBody.() -> Unit) {
        group("Scenario: $description", defaultCachingMode = CachingMode.SCOPE) {
            body(ScenarioBody(this))
        }
    }
}

@SpekDsl
class ScenarioBody(delegate: GroupBody): GroupBody by delegate {
    @Synonym(SynonymType.TEST, prefix = "Given: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Given(description: String, body: TestBody.() -> Unit) {
        test("Given: $description", body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "When: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun When(description: String, body: TestBody.() -> Unit) {
        test("When: $description", body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "Then: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Then(description: String, body: TestBody.() -> Unit) {
        test("Then: $description", body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "And: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun And(description: String, body: TestBody.() -> Unit) {
        test("And: $description", body = body)
    }
}
