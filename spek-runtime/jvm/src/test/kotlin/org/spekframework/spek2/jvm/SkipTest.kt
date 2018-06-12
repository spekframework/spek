package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip

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
}
