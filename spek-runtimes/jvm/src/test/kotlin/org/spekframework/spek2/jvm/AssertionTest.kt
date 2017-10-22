package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class AssertionTest: AbstractSpekRuntimeTest() {
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
