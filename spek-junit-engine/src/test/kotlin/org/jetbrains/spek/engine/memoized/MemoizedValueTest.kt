package org.jetbrains.spek.engine.memoized

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.engine.scope.Scope
import org.junit.jupiter.api.Test
import org.junit.platform.engine.UniqueId

/**
 * @author Ranie Jade Ramiso
 */
class MemoizedValueTest {
    class Foo
    @Test
    fun testSucceedingCallsToGetWithCachingModeTest() {
        val helper = MemoizedValue(CachingMode.TEST, { Foo() })
        val test = Scope.Test(UniqueId.root("test", "test"), Pending.No, {})
        val first = helper.get()
        helper.beforeTest(test)
        assertThat(first, !sameInstance(helper.get()))
    }

    @Test
    fun testSucceedingCallsToGetWithCachingModeGroup() {
        val helper = MemoizedValue(CachingMode.GROUP, { Foo() })
        val test = Scope.Test(UniqueId.root("test", "test"), Pending.No, {})
        val first = helper.get()
        helper.beforeTest(test)
        assertThat(first, sameInstance(helper.get()))
    }
}
