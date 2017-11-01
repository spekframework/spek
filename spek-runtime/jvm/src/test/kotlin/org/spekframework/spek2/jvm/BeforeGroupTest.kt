package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class BeforeGroupTest: AbstractSpekRuntimeTest() {
    @Test
    fun testBeforeGroup() {
        class TestSpek: Spek({
            var counter = 0

            beforeGroup { counter++ }

            test("should be 1") {
                assertThat(counter, equalTo(1))
            }

            group("nested group") {
                beforeGroup { counter++ }

                test("should be 2") {
                    assertThat(counter, equalTo(2))
                }
            }
        })

        val recorder = executeTestsForClass(TestSpek::class)

        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }
}
