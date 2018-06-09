package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.scope.PathBuilder

class FilterTest: AbstractSpekRuntimeTest() {
    @Test
    fun filter() {
        class TestSpek: Spek({
            group("group") {
                test("test") {
                    counter++
                }
                test("another test") {}
            }
        })

        val path = PathBuilder.from(TestSpek::class)
            .append("group")
            .append("test")
            .build()

        val recorder = executeTestsForPath(path)

        assertThat(recorder.testSuccessfulCount, equalTo(1))
        assertThat(counter, equalTo(1))
    }

    companion object {
        var counter = 0
    }
}
