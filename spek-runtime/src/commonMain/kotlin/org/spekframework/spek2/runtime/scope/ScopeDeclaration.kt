package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.runtime.lifecycle.MemoizedValueAdapter2

sealed class ScopeDeclaration {
    enum class FixtureType {
        BEFORE_GROUP,
        AFTER_GROUP,
        BEFORE_EACH_TEST,
        AFTER_EACH_TEST,

        // future types
        BEFORE_EACH_GROUP,
        AFTER_EACH_GROUP
    }
    class Fixture(val type: FixtureType, val cb: () -> Unit): ScopeDeclaration()
    class Memoized<T>(val name: String, val cachingMode: CachingMode, val adapter: MemoizedValueAdapter2<T>): ScopeDeclaration()

    companion object {
        fun beforeGroup(cb: () -> Unit) = Fixture(FixtureType.BEFORE_GROUP, cb)
        fun afterGroup(cb: () -> Unit) = Fixture(FixtureType.AFTER_GROUP, cb)

        fun beforeEachTest(cb: () -> Unit) = Fixture(FixtureType.BEFORE_EACH_TEST, cb)
        fun afterEachTest(cb: () -> Unit) = Fixture(FixtureType.AFTER_EACH_TEST, cb)

        fun <T> memoized(name: String, cachingMode: CachingMode, adapter: MemoizedValueAdapter2<T>) = Memoized(name, cachingMode, adapter)
    }
}
