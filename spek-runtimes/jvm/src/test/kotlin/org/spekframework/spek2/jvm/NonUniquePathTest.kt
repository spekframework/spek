package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.it
import org.spekframework.spek2.jvm.support.AbstractSpekJvmRuntimeTest

class NonUniquePathTest: AbstractSpekJvmRuntimeTest() {
    @Test
    fun nonUnique() {
        class NonUniqueSpek: Spek({
            group("some group") {
                it("duplicate") { }
                it("duplicate") { }
            }
        })

        val recorder = executeTestsForClass(NonUniqueSpek::class)

        assertThat(recorder.testSuccessfulCount, equalTo(2))
    }
}
