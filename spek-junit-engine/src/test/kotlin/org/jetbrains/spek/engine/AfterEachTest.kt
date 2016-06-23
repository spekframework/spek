package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.gen5.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class AfterEachTest: AbstractSpekTestEngineTest() {
    @Test
    fun testAfterEach() {
        counter = 0

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

    @Test
    fun testBeforeEachFailure() {
        counter = 0

        class TestSpek: Spek({
            group("group") {
                beforeEach { assertThat(true, equalTo(false)) }
                test("test") { }
                test("another test") { }
                afterEach { counter++ }
            }

        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testFailureCount, equalTo(2))
        assertThat(counter, equalTo(0))
    }

    @Test
    fun testTestFailure() {
        counter = 0

        class TestSpek: Spek({
            group("group") {
                test("test") { }
                test("another test") { }
                test("failing test") { assertThat(true, equalTo(false)) }
                afterEach { counter++ }
            }

        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testFailureCount, equalTo(1))
        assertThat(counter, equalTo(2))
    }

    companion object {
        var counter = 0
    }
}
