package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

/**
 * @author Ranie Jade Ramiso
 */
class AbstractSpekTest: AbstractSpekRuntimeTest() {
    abstract class SomeAbstractSpek: Spek({})

    @Test
    fun testIgnoreAbstractClass() {
        val recorder = executeTestsForClass(SomeAbstractSpek::class)
        assertThat(recorder.testStartedCount, equalTo(0))
    }
}
