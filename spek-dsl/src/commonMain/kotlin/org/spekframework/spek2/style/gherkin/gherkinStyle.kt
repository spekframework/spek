package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.meta.*

@Synonym(SynonymType.GROUP, prefix = "Feature: ")
@Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
fun GroupBody.Feature(description: String, body: FeatureBody.() -> Unit) {
    group("Feature: $description", defaultCachingMode = CachingMode.EACH_GROUP) {
        body(FeatureBody(this))
    }
}

@SpekDsl
class FeatureBody(val delegate: GroupBody): LifecycleAware by delegate {
    var defaultTimeout: Long
        get() = delegate.defaultTimeout
        set(value) { delegate.defaultTimeout = value }

    @Synonym(SynonymType.GROUP, prefix = "Scenario: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Scenario(description: String, body: ScenarioBody.() -> Unit) {
        delegate.group("Scenario: $description", defaultCachingMode = CachingMode.SCOPE, preserveExecutionOrder = true, failFast = true) {
            body(ScenarioBody(this))
        }
    }
}

@SpekDsl
class ScenarioBody(val delegate: GroupBody): LifecycleAware by delegate {
    var defaultTimeout: Long
        get() = delegate.defaultTimeout
        set(value) { delegate.defaultTimeout = value }

    @Synonym(SynonymType.TEST, prefix = "Given: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Given(description: String, timeout: Long = defaultTimeout, body: TestBody.() -> Unit) {
        delegate.test("Given: $description", timeout = timeout, body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "When: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun When(description: String, timeout: Long = defaultTimeout, body: TestBody.() -> Unit) {
        delegate.test("When: $description", timeout = timeout, body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "Then: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Then(description: String, timeout: Long = defaultTimeout, body: TestBody.() -> Unit) {
        delegate.test("Then: $description", timeout = timeout, body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "And: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun And(description: String, timeout: Long = defaultTimeout, body: TestBody.() -> Unit) {
        delegate.test("And: $description", timeout = timeout, body = body)
    }
}
