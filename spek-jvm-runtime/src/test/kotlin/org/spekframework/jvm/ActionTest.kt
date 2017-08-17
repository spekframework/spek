package org.spekframework.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Test
import org.spekframework.jvm.support.AbstractSpekJvmRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class ActionTest: AbstractSpekJvmRuntimeTest() {
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
