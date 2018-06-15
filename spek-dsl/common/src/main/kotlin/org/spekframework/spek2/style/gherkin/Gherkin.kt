package org.spekframework.spek2.style.gherkin

import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Order
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue
import org.spekframework.spek2.meta.*

@SpekDsl
class Feature(private val delegate: GroupBody) : GroupBody by delegate {
    @Synonym(SynonymType.GROUP, prefix = "Scenario: ")
    @Descriptions(Description(DescriptionLocation.VALUE_PARAMETER, 0))
    fun Scenario(description: String, skip: Skip = Skip.No, body: Scenario.() -> Unit) {
        group("Scenario: $description", skip, order = Order.Unspecified) {
            body(Scenario(this))
        }
    }

    fun <T> memoized(factory: () -> T): MemoizedValue<T> {
        // Override CachingMode to GROUP to properly support Gherkin.
        return delegate.memoized(CachingMode.GROUP, factory)
    }

    fun <T> memoized(factory: () -> T, destructor: (T) -> Unit): MemoizedValue<T> {
        // Override CachingMode to GROUP to properly support Gherkin.
        return delegate.memoized(CachingMode.GROUP, factory, destructor)
    }

    @Deprecated(
            message = "memoized with custom CachingMode is not supported in Gherkin style.",
            replaceWith = ReplaceWith("memoized"),
            level = DeprecationLevel.ERROR
    )
    override fun <T> memoized(mode: CachingMode, factory: () -> T): MemoizedValue<T> = throwUnsupportedMemoized()

    @Deprecated(
            message = "memoized with custom CachingMode is not supported in Gherkin style.",
            replaceWith = ReplaceWith("memoized"),
            level = DeprecationLevel.ERROR
    )
    override fun <T> memoized(mode: CachingMode, factory: () -> T, destructor: (T) -> Unit): MemoizedValue<T> = throwUnsupportedMemoized()

    private fun throwUnsupportedMemoized(): Nothing = throw UnsupportedOperationException("memoized with custom CachingMode is not supported in Gherkin.")
}

// Give, When, Then and And don't need synonym annotations since
// they should not be executed individually.
@SpekDsl
class Scenario(delegate: GroupBody) : GroupBody by delegate {

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
