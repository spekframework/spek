package org.spekframework.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.junit.jupiter.api.Test
import org.spekframework.jvm.support.AbstractSpekJvmRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class AbstractSpekTest: AbstractSpekJvmRuntimeTest() {
    abstract class SomeAbstractSpek: Spek({})

    @Test
    fun testIgnoreAbstractClass() {
        val recorder = executeTestsForClass(SomeAbstractSpek::class)
        assertThat(recorder.testStartedCount, equalTo(0))
    }
}
