package org.jetbrains.spek.subject

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Test
import org.spekframework.jvm.SpekJvmRuntime
import org.spekframework.jvm.classToPath
import org.spekframework.runtime.scope.Path
import org.spekframework.runtime.test.AbstractSpekRuntimeTest
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
