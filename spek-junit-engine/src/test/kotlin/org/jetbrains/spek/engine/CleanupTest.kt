package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.gen5.api.Test
import java.io.Closeable

class CleanupTest: AbstractSpekTestEngineTest() {

    class TestCloseable : Closeable {
        var closed = false ; private set
        override fun close() { closed = true }
    }

    @Test
    fun testSuccess() {
        closeable = TestCloseable()
        class TestSpek: Spek({
            group("group") {
                test("test") {
                    closeable.autoCleanup()
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(closeable.closed, equalTo(true))
    }

    @Test
    fun testFailure() {
        closeable = TestCloseable()
        class TestSpek: Spek({
            group("group") {
                test("test") {
                    closeable.autoCleanup()
                    assertThat(true, equalTo(false))
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(closeable.closed, equalTo(true))
    }

    @Test
    fun testBeforeEachFail() {
        closeable = TestCloseable()
        class TestSpek: Spek({
            group("group") {
                beforeEach {
                    closeable.autoCleanup()
                    assertThat(true, equalTo(false))
                }
                test("test") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(closeable.closed, equalTo(true))
    }

    @Test
    fun testPreviousBeforeEachFail() {
        closeable = TestCloseable()
        class TestSpek: Spek({
            group("group") {
                beforeEach {
                    assertThat(true, equalTo(false))
                }
                beforeEach {
                    closeable.autoCleanup()
                }
                test("test") { }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(closeable.closed, equalTo(false))
    }

    @Test
    fun testAfterEachFail() {
        closeable = TestCloseable()
        class TestSpek: Spek({
            group("group") {
                afterEach {
                    assertThat(true, equalTo(false))
                }
                test("test") {
                    closeable.autoCleanup()
                }
            }
        })

        executeTestsForClass(TestSpek::class)
        assertThat(closeable.closed, equalTo(true))
    }

    companion object {
        lateinit var closeable: TestCloseable
    }

}