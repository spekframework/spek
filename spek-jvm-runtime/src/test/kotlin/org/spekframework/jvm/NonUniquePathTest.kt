package org.spekframework.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Test
import org.spekframework.jvm.support.AbstractSpekJvmRuntimeTest

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
