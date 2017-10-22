package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Pending
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class PendingTest: AbstractSpekRuntimeTest() {
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

        assertThat(recorder.containerIgnoredCount, equalTo(1))
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
        assertThat(recorder.testIgnoredCount, equalTo(1))
    }
}
