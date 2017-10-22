package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class FailingGroupTest: AbstractSpekRuntimeTest() {
    @Test
    fun testFailingGroup() {
        class FailingSpec: Spek({

            group("some failing group") {
                throw RuntimeException()
                test("this won't be executed") { }
            }

            group("some group") {
                test("do something") { }
                test("do another thing") { }
            }
        })

        val recorder = executeTestsForClass(FailingSpec::class)

        assertThat(recorder.containerFailureCount, equalTo(1))
        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }
}
