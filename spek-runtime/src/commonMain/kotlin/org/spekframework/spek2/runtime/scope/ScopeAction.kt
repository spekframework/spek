package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.runtime.execution.ExecutionResult

sealed class ScopeAction {
    enum class FixtureType {
        BEFORE_GROUP,
        AFTER_GROUP,
        BEFORE_EACH_TEST,
        AFTER_EACH_TEST,

        // future types
        BEFORE_EACH_GROUP,
        AFTER_EACH_GROUP
    }
    class Fixture(val type: FixtureType, val cb: () -> Unit): ScopeAction()
    class Value<T>(val cachingMode: CachingMode, val factory: () -> T, val destructor: (T) -> Unit): ScopeAction()

    // group markers
    class GroupStart(val group: GroupScopeImpl): ScopeAction()
    class GroupEnd(val group: GroupScopeImpl, val result: ExecutionResult): ScopeAction()
    class GroupIgnored(val group: GroupScopeImpl, val reason: String): ScopeAction()

    companion object {
        fun beforeGroup(cb: () -> Unit) = Fixture(FixtureType.BEFORE_GROUP, cb)
        fun afterGroup(cb: () -> Unit) = Fixture(FixtureType.AFTER_GROUP, cb)

        fun beforeEachTest(cb: () -> Unit) = Fixture(FixtureType.BEFORE_EACH_TEST, cb)
        fun afterEachTest(cb: () -> Unit) = Fixture(FixtureType.AFTER_EACH_TEST, cb)

        fun <T> value(cachingMode: CachingMode, factory: () -> T, destructor: (T) -> Unit) = Value(cachingMode, factory, destructor)
    }
}
