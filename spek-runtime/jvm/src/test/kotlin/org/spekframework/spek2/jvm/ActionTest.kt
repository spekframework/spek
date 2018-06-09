package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek

class ActionTest: AbstractSpekRuntimeTest() {
    @Test
    fun testOn() {
        class TestSpek: Spek({
            var count = 0
            action("something") {
                count++

                test("do this") {
                    assertThat(count, equalTo(1))
                }

                count++

                test("do that") {
                    assertThat(count, equalTo(2))
                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }
}
