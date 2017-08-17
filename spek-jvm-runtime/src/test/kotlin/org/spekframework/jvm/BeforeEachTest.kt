package org.spekframework.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.junit.jupiter.api.Test
import org.spekframework.jvm.support.AbstractSpekJvmRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class BeforeEachTest: AbstractSpekJvmRuntimeTest() {
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
