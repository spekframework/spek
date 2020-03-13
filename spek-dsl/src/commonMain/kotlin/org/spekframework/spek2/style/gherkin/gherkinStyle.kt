package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.Fixture
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.meta.Description
import org.spekframework.spek2.meta.DescriptionLocation
import org.spekframework.spek2.meta.Descriptions
import org.spekframework.spek2.meta.SpekDsl
import org.spekframework.spek2.meta.Synonym
import org.spekframework.spek2.meta.SynonymType

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

    fun beforeEachScenario(fixture: Fixture) = delegate.beforeEachGroup(fixture)
    fun afterEachScenario(fixture: Fixture) = delegate.afterEachGroup(fixture)
    fun beforeFeature(fixture: Fixture) = delegate.beforeGroup(fixture)
    fun afterFeature(fixture: Fixture) = delegate.afterGroup(fixture)

    @Deprecated("Use beforeEachScenario instead", ReplaceWith("beforeEachScenario(fixture)"))
    override fun beforeEachGroup(fixture: Fixture) = beforeEachScenario(fixture)

    @Deprecated("Use afterEachScenario instead", ReplaceWith("afterEachScenario(fixture)"))
    override fun afterEachGroup(fixture: Fixture) = afterEachScenario(fixture)

    @Deprecated("Use beforeFeature instead", ReplaceWith("beforeFeature(fixture)"))
    override fun beforeGroup(fixture: Fixture) = beforeFeature(fixture)

    @Deprecated("Use afterFeature instead", ReplaceWith("afterFeature(fixture)"))
    override fun afterGroup(fixture: Fixture) = afterFeature(fixture)
}

@SpekDsl
class ScenarioBody(val delegate: GroupBody): LifecycleAware by delegate {
    var defaultTimeout: Long
        get() = delegate.defaultTimeout
        set(value) { delegate.defaultTimeout = value }

    @Synonym(SynonymType.TEST, prefix = "Given: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Given(description: String, timeout: Long = defaultTimeout, body: suspend TestBody.() -> Unit) {
        delegate.test("Given: $description", timeout = timeout, body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "When: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun When(description: String, timeout: Long = defaultTimeout, body: suspend TestBody.() -> Unit) {
        delegate.test("When: $description", timeout = timeout, body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "Then: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Then(description: String, timeout: Long = defaultTimeout, body: suspend TestBody.() -> Unit) {
        delegate.test("Then: $description", timeout = timeout, body = body)
    }

    @Synonym(SynonymType.TEST, prefix = "And: ", runnable = false)
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun And(description: String, timeout: Long = defaultTimeout, body: suspend TestBody.() -> Unit) {
        delegate.test("And: $description", timeout = timeout, body = body)
    }

    fun beforeScenario(fixture: Fixture) = delegate.beforeGroup(fixture)
    fun afterScenario(fixture: Fixture) = delegate.afterGroup(fixture)
    fun beforeEachStep(fixture: Fixture) = delegate.beforeEachTest(fixture)
    fun afterEachStep(fixture: Fixture) = delegate.afterEachTest(fixture)

    @Deprecated("Use beforeEachStep instead", ReplaceWith("beforeEachStep(fixture)"))
    override fun beforeEachTest(fixture: Fixture) = beforeEachStep(fixture)

    @Deprecated("Use afterEachStep instead", ReplaceWith("afterEachStep(fixture)"))
    override fun afterEachTest(fixture: Fixture) = afterEachStep(fixture)

    @Deprecated("Use before scenario instead", ReplaceWith("beforeScenario(fixture)"))
    override fun beforeGroup(fixture: Fixture) = beforeScenario(fixture)

    @Deprecated("Use after scenario instead", ReplaceWith("afterScenario(fixture)"))
    override fun afterGroup(fixture: Fixture) = afterScenario(fixture)
}
