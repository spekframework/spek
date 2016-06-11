package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.gen5.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class AssertionTest: AbstractSpekTestEngineTest() {
    @Test
    fun testPassingSpek() {
        class TestSpek: Spek({
            group("group") {
                test("test") {}
                test("another test") {}
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }

    @Test
    fun testFailingSpek() {
        class TestSpek: Spek({
            group("group") {
                test("test") { assertThat(true, equalTo(false)) }
                test("another test") {}
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testSuccessfulCount, equalTo(1))
        assertThat(recorder.testFailureCount, equalTo(1))
    }
}
