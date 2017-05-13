package org.jetbrains.spek.subject

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.engine.SpekTestEngine
import org.jetbrains.spek.engine.test.AbstractJUnitTestEngineTest
import org.junit.jupiter.api.Test
import java.util.ArrayDeque
import java.util.LinkedList
import java.util.Queue

class IncludeSubjectTest: AbstractJUnitTestEngineTest<SpekTestEngine>(SpekTestEngine()) {

    @Test
    fun itShouldOverrideNestedSubject() {
        class JavaQueueSpec : SubjectSpek<Queue<String>>({
            subject {
                LinkedList()
            }

            describe("whatever") {
                on("pushing an item") {
                    subject.offer("Hi")
                }
            }
        })

        class ArrayDequeSpec : SubjectSpek<ArrayDeque<String>>({
            subject {
                // all tests should fail because of this
                throw Throwable()
            }

            itBehavesLike(JavaQueueSpec())
        })

        val recorder = executeTestsForClass(ArrayDequeSpec::class)
        assertThat(recorder.testSuccessfulCount, equalTo(0))
    }
}
