package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.LifecycleAware
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
class FeatureBody(val delegate: GroupBody): LifecycleAware by delegate {
    @Synonym(SynonymType.GROUP, prefix = "Scenario: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Scenario(description: String, body: ScenarioBody.() -> Unit) {
        delegate.group("Scenario: $description", defaultCachingMode = CachingMode.SCOPE) {
            body(ScenarioBody(this))
        }
    }
}

@SpekDsl
class ScenarioBody(val delegate: GroupBody): LifecycleAware by delegate {
    fun Given(description: String, body: TestBody.() -> Unit) {
        delegate.test("Given: $description", body = body)
    }

    fun When(description: String, body: TestBody.() -> Unit) {
        delegate.test("When: $description", body = body)
    }

    fun Then(description: String, body: TestBody.() -> Unit) {
        delegate.test("Then: $description", body = body)
    }

    fun And(description: String, body: TestBody.() -> Unit) {
        delegate.test("And: $description", body = body)
    }
}
