package org.jetbrains.spek.engine.memoized

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.dsl.Pending
import org.jetbrains.spek.engine.scope.Scope
import org.junit.gen5.api.Test
import org.junit.gen5.engine.UniqueId

/**
 * @author Ranie Jade Ramiso
 */
class MemoizedHelperTest {
    @Test
    fun testSucceedingCallsToGet() {
        val helper = MemoizedHelper<List<String>>({ emptyList() })
        assertThat(helper.get(), equalTo(helper.get()))
    }

    @Test
    fun testBeforeTestResetsHelper() {
        val helper = MemoizedHelper<List<String>>({ emptyList() })
        val test = Scope.Test(UniqueId.root("test", "test"), Pending.No, {})
        val first = helper.get()
        helper.beforeTest(test)
        assertThat(first, equalTo(helper.get()))
    }
}
