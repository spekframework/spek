package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.gen5.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class AfterEachTest: AbstractSpekTestEngineTest() {
    @Test
    fun testAfterEach() {
        class TestSpek: Spek({
            group("group") {
                test("test") { }
                test("another test") { }
                afterEach { counter++ }
            }
            afterEach { counter++ }

        })

        executeTestsForClass(TestSpek::class)
        assertThat(counter, equalTo(4))
    }

    @Test
    fun testAfterEachFailure() {
        class TestSpek: Spek({
            group("group") {
                test("test") { }
                test("another test") { }
                afterEach { assertThat(true, equalTo(false)) }
            }

        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testFailureCount, equalTo(2))
    }

    companion object {
        var counter = 0
    }
}
