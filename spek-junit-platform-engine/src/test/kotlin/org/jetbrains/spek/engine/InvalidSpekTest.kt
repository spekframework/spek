package org.jetbrains.spek.engine

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class InvalidSpekTest: AbstractSpekTestEngineTest() {

    @Test
    fun testIgnoreInvalidSpek() {
        val recorder = executeForPackage("org.jetbrains.spek.engine.packageWithInvalidSpek")
        assertThat(recorder.testStartedCount, equalTo(0))
    }

}
