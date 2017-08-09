package org.spekframework.spek2.subject

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.describe
import org.spekframework.spek2.dsl.on
import org.spekframework.spek2.jvm.SpekJvmRuntime
import org.spekframework.spek2.jvm.classToPath
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.test.AbstractSpekRuntimeTest
import java.util.ArrayDeque
import java.util.LinkedList
import java.util.Queue
import kotlin.reflect.KClass

class IncludeSubjectTest: AbstractSpekRuntimeTest<SpekJvmRuntime>(SpekJvmRuntime()) {

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

    // FIXME: duplicate
    override fun toPath(spek: KClass<out Spek>): Path {
        return classToPath(spek)
    }
}
