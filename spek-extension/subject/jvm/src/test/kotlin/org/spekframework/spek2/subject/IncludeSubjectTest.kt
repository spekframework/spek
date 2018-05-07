package org.spekframework.spek2.subject

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest
import java.util.*

class IncludeSubjectTest: AbstractSpekRuntimeTest() {

    @Test
    fun itShouldOverrideNestedSubject() {
        class JavaQueueSpec : SubjectSpek<Queue<String>>({
            val subject by subject {
                LinkedList()
            }

            group("whatever") {
                test("pushing an item") {
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
