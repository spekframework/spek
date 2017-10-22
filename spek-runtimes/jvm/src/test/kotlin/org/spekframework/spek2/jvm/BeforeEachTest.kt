package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class BeforeEachTest: AbstractSpekRuntimeTest() {
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
    fun testBeforeEachLazyAction() {
        counter = 0
        class TestSpek: Spek({
            beforeEachTest { counter++ }
            action("group") {
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
