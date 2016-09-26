package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class ActionTest: AbstractSpekTestEngineTest() {
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
    }
}
