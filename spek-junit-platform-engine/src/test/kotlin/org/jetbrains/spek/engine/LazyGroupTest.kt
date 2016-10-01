package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.Test

/**
 * @author Ranie Jade Ramiso
 */
class LazyGroupTest: AbstractSpekTestEngineTest() {
    @Test
    fun testBeforeEachShouldFail() {
        class TestSpek: Spek({
            on("fail") {
                beforeEachTest {
                    println()
                }

                it("will not show") {

                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.containerFailureCount, equalTo(1))
        assertThat(recorder.testStartedCount, equalTo(0))
    }

    @Test
    fun testAfterEachShouldFail() {
        class TestSpek: Spek({
            on("fail") {
                it("will not show") {

                }

                afterEachTest {
                    println()
                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.containerFailureCount, equalTo(1))
        assertThat(recorder.testStartedCount, equalTo(0))
    }

    @Test
    fun testGroupCreationFail() {
        class TestSpek: Spek({
            on("fail") {
                group("foo") {

                }

                it("will not show") {

                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.containerFailureCount, equalTo(1))
        assertThat(recorder.testStartedCount, equalTo(0))
    }

    @Test
    fun testOn() {
        class TestSpek: Spek({
            var count = 0
            on("something") {
                count++

                it("do this") {
                    assertThat(count, equalTo(1))
                }

                count++

                it("do that") {
                    assertThat(count, equalTo(2))
                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.dynamicTestRegisteredCount, equalTo(2))
        assertThat(recorder.testSuccessfulCount, equalTo(1))
        assertThat(recorder.testFailureCount, equalTo(1))

        class SomeSpec: Spek({
            describe("something") {
                beforeEachTest {
                    println("hello")
                }

                on("this") {
                    println("world")
                    it("do this") {
                        println("foo")
                    }

                    it("do that") {
                        println("bar")
                    }
                }
            }
        })
    }
}
