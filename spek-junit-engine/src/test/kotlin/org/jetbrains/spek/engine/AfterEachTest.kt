package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * @author Ranie Jade Ramiso
 */
@RunWith(JUnitPlatform::class)
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

    @Test
    fun testAfterEachShouldStillBeInvokedWhenTestFails() {
        counter = 0
        class TestSpek: Spek({
            group("another group") {
                test("test") {
                    assertThat(true, equalTo(false))
                }

                afterEach { counter++ }
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
                beforeEach {
                    assertThat(true, equalTo(false))
                }
                test("test") { }

                afterEach { counter++ }
            }
        })

        executeTestsForClass(TestSpek::class)

        assertThat(counter, equalTo(1))

    }

    companion object {
        var counter = 0
    }
}
