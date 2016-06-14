package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.gen5.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class BeforeEachTest: AbstractSpekTestEngineTest() {
    @Test
    fun testBeforeEach() {
        class TestSpek: Spek({
            beforeEach { counter++ }
            group("group") {
                beforeEach { counter++ }
                test("test") { }
                test("another test") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(counter, equalTo(4))
    }

    @Test
    fun testBeforeEachFailure() {
        class TestSpek: Spek({
            group("group") {
                beforeEach { assertThat(true, equalTo(false)) }
                test("test") { }
                test("another test") { }
            }

        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testFailureCount, equalTo(2))
    }

    companion object {
        var counter = 0
    }
}
