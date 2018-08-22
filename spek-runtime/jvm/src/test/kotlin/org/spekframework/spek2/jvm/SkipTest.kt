package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.style.specification.xdescribe

class SkipTest : AbstractSpekRuntimeTest() {
    @Test
    fun testSkipGroup() {
        class SkipSpek : Spek({
            group("foo") {
                test("bar") { }
            }

            group("a skip foo", Skip.Yes()) {
                test("a bar inside a skip foo") { }
            }
        })

        val recorder = executeTestsForClass(SkipSpek::class)

        assertThat(recorder.containerIgnoredCount, equalTo(1))
    }

    @Test
    fun testSkipTest() {
        class SkipSpek : Spek({
            group("foo") {
                test("bar") { }
                test("a skip bar", skip = Skip.Yes()) { }
            }
        })

        val recorder = executeTestsForClass(SkipSpek::class)

        assertThat(recorder.testStartedCount, equalTo(1))
        assertThat(recorder.testIgnoredCount, equalTo(1))
    }

    @Test
    fun testSkipEntireSpek() {
        class SkipEntireSpek : Spek({
            group("foo", Skip.Yes()) {

            }
        })

        val recorder = executeTestsForClass(SkipEntireSpek::class)

        assertThat(recorder.containerIgnoredCount, equalTo(1))
        assertThat(recorder.containerFailureCount, equalTo(0))
        assertThat(recorder.containerStartedCount, equalTo(1)) // Root container.
        assertThat(recorder.containerFinishedCount, equalTo(1))

        assertThat(recorder.testStartedCount, equalTo(0))
        assertThat(recorder.testIgnoredCount, equalTo(0))
        assertThat(recorder.testFailureCount, equalTo(0))
    }
}
