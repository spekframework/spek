package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class BeforeGroupTest: AbstractSpekTestEngineTest() {
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
