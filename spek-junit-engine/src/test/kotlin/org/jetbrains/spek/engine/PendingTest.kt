package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.Spek
import org.jetbrains.spek.dsl.Pending
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.gen5.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class PendingTest: AbstractSpekTestEngineTest() {
    @Test
    fun testPendingGroup() {
        class PendingSpek: Spek({
            group("foo") {
                test("bar") { }
            }

            group("a pending foo", Pending.Yes()) {
                test("a bar inside a pending foo") { }
            }
        })

        val recorder = executeTestsForClass(PendingSpek::class)

        assertThat(recorder.containerStartedCount, equalTo(3))
        assertThat(recorder.containerSkippedCount, equalTo(1))
    }

    @Test
    fun testPendingTest() {
        class PendingSpek: Spek({
            group("foo") {
                test("bar") { }
                test("a pending bar", pending = Pending.Yes()) { }
            }
        })

        val recorder = executeTestsForClass(PendingSpek::class)

        assertThat(recorder.testStartedCount, equalTo(1))
        assertThat(recorder.testSkippedCount, equalTo(1))
    }
}
