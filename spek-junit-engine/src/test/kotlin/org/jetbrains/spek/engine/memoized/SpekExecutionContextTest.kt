package org.jetbrains.spek.engine.memoized

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.dsl.Pending
import org.jetbrains.spek.engine.ExecutionListener
import org.jetbrains.spek.engine.SpekExecutionContext
import org.jetbrains.spek.engine.scope.Scope
import org.junit.gen5.api.Test
import org.junit.gen5.engine.UniqueId
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ranie Jade Ramiso
 */
class SpekExecutionContextTest {
    @Test
    fun testBeforeTestInvokesListeners() {
        val context = SpekExecutionContext()
        val flag = AtomicBoolean(false)
        context.addListener(object: ExecutionListener {
            override fun beforeTest(test: Scope.Test) {
                flag.set(true)
            }
        })

        context.beforeTest(Scope.Test(UniqueId.root("test", "test"), Pending.No, {}))

        assertThat(flag.get(), equalTo(true))
    }

    @Test
    fun testAfterTestInvokesListeners() {
        val context = SpekExecutionContext()
        val flag = AtomicBoolean(false)
        context.addListener(object: ExecutionListener {
            override fun afterTest(test: Scope.Test) {
                flag.set(true)
            }
        })

        context.afterTest(Scope.Test(UniqueId.root("test", "test"), Pending.No, {}))

        assertThat(flag.get(), equalTo(true))
    }
}
