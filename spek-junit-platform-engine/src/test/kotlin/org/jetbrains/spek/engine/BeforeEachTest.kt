package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class BeforeEachTest: AbstractSpekTestEngineTest() {
    @Test
    fun testBeforeEach() {
        counter = 0
        class TestSpek: Spek({
            beforeEachTest { counter++ }
            group("group") {
                beforeEachTest { counter++ }
                test("test") { }
                test("another test") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(counter, equalTo(4))
    }

    @Test
    fun testBeforeEachLazyGroup() {
        counter = 0
        class TestSpek: Spek({
            beforeEachTest { counter++ }
            group("group", lazy = true) {
                test("test") { }
                test("another test") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(counter, equalTo(1))
    }

    @Test
    fun testBeforeEachFailure() {
        class TestSpek: Spek({
            group("group") {
                beforeEachTest { assertThat(true, equalTo(false)) }
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
