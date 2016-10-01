package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

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
                afterEachTest { counter++ }
            }
            afterEachTest { counter++ }

        })

        executeTestsForClass(TestSpek::class)
        assertThat(counter, equalTo(4))
    }

    @Test
    fun testAfterEachLazyGroup() {
        counter = 0
        class TestSpek: Spek({
            group("group", lazy = true) {
                test("test") { }
                test("another test") { }
            }
            afterEachTest { counter++ }

        })

        executeTestsForClass(TestSpek::class)
        assertThat(counter, equalTo(1))
    }

    @Test
    fun testAfterEachFailure() {
        class TestSpek: Spek({
            group("group") {
                test("test") { }
                test("another test") { }
                afterEachTest { assertThat(true, equalTo(false)) }
            }

        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testFailureCount, equalTo(2))
    }

    @Test
    fun testAfterEachShouldStillBeInvokedWhenTestFails() {
        counter = 0
        class TestSpek: Spek({
            group("another group") {
                test("test") {
                    assertThat(true, equalTo(false))
                }

                afterEachTest { counter++ }
            }
        })

        executeTestsForClass(TestSpek::class)

        assertThat(counter, equalTo(1))

    }

    @Test
    fun testAfterEachShouldStillBeInvokedWhenBeforeEach() {
        counter = 0
        class TestSpek: Spek({
            group("another group") {
                beforeEachTest {
                    assertThat(true, equalTo(false))
                }
                test("test") { }

                afterEachTest { counter++ }
            }
        })

        executeTestsForClass(TestSpek::class)

        assertThat(counter, equalTo(1))

    }

    companion object {
        var counter = 0
    }
}
