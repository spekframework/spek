package org.spekframework.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.Pending
import org.junit.jupiter.api.Test
import org.spekframework.jvm.support.AbstractSpekJvmRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class PendingTest: AbstractSpekJvmRuntimeTest() {
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
