package org.jetbrains.spek.engine.memoized

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.dsl.Pending
import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.engine.scope.Scope
import org.junit.gen5.api.Test
import org.junit.gen5.engine.UniqueId

/**
 * @author Ranie Jade Ramiso
 */
class MemoizedHelperTest {
    class Foo
    @Test
    fun testSucceedingCallsToGetWithCachingModeTest() {
        val helper = MemoizedHelper(CachingMode.TEST, { Foo() })
        val test = Scope.Test(UniqueId.root("test", "test"), Pending.No, {})
        val first = helper.get()
        helper.beforeTest(test)
        assertThat(first, !sameInstance(helper.get()))
    }

    @Test
    fun testSucceedingCallsToGetWithCachingModeGroup() {
        val helper = MemoizedHelper(CachingMode.GROUP, { Foo() })
        val test = Scope.Test(UniqueId.root("test", "test"), Pending.No, {})
        val first = helper.get()
        helper.beforeTest(test)
        assertThat(first, sameInstance(helper.get()))
    }
}
