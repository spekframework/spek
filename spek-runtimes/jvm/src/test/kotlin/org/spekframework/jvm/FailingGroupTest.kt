package org.spekframework.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.junit.jupiter.api.Test
import org.spekframework.jvm.support.AbstractSpekJvmRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class FailingGroupTest: AbstractSpekJvmRuntimeTest() {
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
