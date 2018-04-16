package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest

class FilterTest: AbstractSpekRuntimeTest() {
    @Test
    fun filter() {
        class TestSpek: Spek({
            group("group") {
                test("test") {}
                test("another test") {}
            }
        })

        val path = PathBuilder.from(TestSpek::class)
            .append("group")
            .append("test")
            .build()

        val recorder = executeTestsforPath(path)

        assertThat(recorder.testSuccessfulCount, equalTo(1))
    }

}
