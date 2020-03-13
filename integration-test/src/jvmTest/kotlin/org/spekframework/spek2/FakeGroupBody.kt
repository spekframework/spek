package org.spekframework.spek2


import org.spekframework.spek2.dsl.Fixture
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue

class FakeGroupBody : GroupBody {
    var beforeEachGroup: Fixture? = null
    var afterEachGroup: Fixture? = null
    var beforeGroup: Fixture? = null
    var afterGroup: Fixture? = null
    var beforeEachTest: Fixture? = null
    var afterEachTest: Fixture? = null

    override fun group(description: String, skip: Skip, defaultCachingMode: CachingMode, preserveExecutionOrder: Boolean, failFast: Boolean, body: GroupBody.() -> Unit) =
            throw UnsupportedOperationException()

    override val defaultCachingMode: CachingMode
        get() = throw UnsupportedOperationException()

    override fun <T> memoized(mode: CachingMode, factory: suspend () -> T): MemoizedValue<T> =
            throw UnsupportedOperationException()

    override fun <T> memoized(mode: CachingMode, factory: suspend () -> T, destructor: suspend (T) -> Unit): MemoizedValue<T> =
            throw UnsupportedOperationException()

    override fun beforeEachTest(fixture: Fixture) {
        beforeEachTest = fixture
    }

    override fun afterEachTest(fixture: Fixture) {
        afterEachTest = fixture
    }

    override fun beforeEachGroup(fixture: Fixture) {
        beforeEachGroup = fixture
    }

    override fun afterEachGroup(fixture: Fixture) {
        afterEachGroup = fixture
    }

    override fun beforeGroup(fixture: Fixture) {
        beforeGroup = fixture
    }

    override fun afterGroup(fixture: Fixture) {
        afterGroup = fixture
    }

    override fun <T> memoized(): MemoizedValue<T> = throw UnsupportedOperationException()

    override var defaultTimeout: Long = 0L

    override fun test(description: String, skip: Skip, timeout: Long, body: suspend TestBody.() -> Unit) = throw UnsupportedOperationException()
}
