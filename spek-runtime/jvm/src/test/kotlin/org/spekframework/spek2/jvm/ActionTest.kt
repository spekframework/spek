package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class ActionTest: AbstractSpekRuntimeTest() {
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

        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }
}
