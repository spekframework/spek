package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class AfterGroupTest: AbstractSpekRuntimeTest() {
    @Test
    fun testAfterGroup() {
        class TestSpek: Spek({
            var counter = 0
            group("nested group") {
                afterGroup { counter++ }

                test("should be 0") {
                    assertThat(counter, equalTo(0))
                }
            }

            group("another nested group") {
                test("should be 1") {
                    assertThat(counter, equalTo(1))
                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }
}
