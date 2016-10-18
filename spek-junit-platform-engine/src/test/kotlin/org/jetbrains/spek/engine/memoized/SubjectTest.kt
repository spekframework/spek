package org.jetbrains.spek.engine.memoized

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.engine.SpekException
import org.jetbrains.spek.engine.support.AbstractSpekTestEngineTest
import org.junit.jupiter.api.Test

/**
 * @author Ranie Jade Ramiso
 */
class SubjectTest: AbstractSpekTestEngineTest() {
    open class Foo {
        fun bar() { }
    }

    // companion objects not allowed on local classes
    class FooSpec: SubjectSpek<Foo>({
        subject { Foo() }
        test("test #1") {
            subject1 = subject
        }

        test("test #2") {
            subject2 = subject
        }
    }) {
        companion object {
            lateinit var subject1: Foo
            lateinit var subject2: Foo
        }
    }

    // companion objects not allowed on local classes
    class Foo2Spec: SubjectSpek<Foo>({
        subject(CachingMode.GROUP) { Foo() }
        test("test #1") {
            subject1 = subject
        }

        test("test #2") {
            subject2 = subject
        }
    }) {
        companion object {
            lateinit var subject1: Foo
            lateinit var subject2: Foo
        }
    }

    class Foo3Spec: SubjectSpek<Foo>({
        subject { Foo() }

        group("lazy", lazy = true) {
            test("test #1") {
                subject1 = subject
            }

            test("test #2") {
                subject2 = subject
            }
        }
    }) {
        companion object {
            lateinit var subject1: Foo
            lateinit var subject2: Foo
        }
    }

    @Test
    fun testDifferentInstancePerTest() {
        executeTestsForClass(FooSpec::class)
        assertThat(FooSpec.subject1, !sameInstance(FooSpec.subject2))
    }

    @Test
    fun testSameInstancePerTest() {
        executeTestsForClass(Foo2Spec::class)
        assertThat(Foo2Spec.subject1, sameInstance(Foo2Spec.subject2))
    }

    @Test
    fun testSubjectOnLazyGroups() {
        executeTestsForClass(Foo3Spec::class)
        assertThat(Foo3Spec.subject1, sameInstance(Foo3Spec.subject2))
    }

    @Test
    fun testNotConfiguredSubject() {
        class SubjectSpec: SubjectSpek<Foo>({
            test("this should fail") {
                subject.bar()
            }
        })

        val recorder = executeTestsForClass(SubjectSpec::class)

        assertThat(recorder.testFailureCount, equalTo(1))
        val throwable = recorder.getFailingTestEvents().first().result.throwable
        fun isSpekException(throwable: Throwable) = throwable is SpekException
        assertThat(throwable.get(), ::isSpekException)
    }

    @Test
    fun testIncludeSubjectSpecInstance() {
        class Bar: Foo()

        class BarSpec: SubjectSpek<Bar>({
            subject { Bar() }
            includeSubjectSpec(FooSpec::class)
        })

        executeTestsForClass(BarSpec::class)
        fun isBar(foo: Foo) = foo is Bar
        assertThat(FooSpec.subject1, ::isBar)
        assertThat(FooSpec.subject2, ::isBar)
    }

    @Test
    fun testIncludeSubjectSpec() {
        open class Foo
        class Bar: Foo()

        class FooSpec: SubjectSpek<Foo>({
            test("a foo test") { }
            test("another foo test") { }
        })

        class BarSpec: SubjectSpek<Bar>({
            subject { Bar() }
            includeSubjectSpec(FooSpec::class)

            test("a bar test") { }
        })

        val recorder = executeTestsForClass(BarSpec::class)

        assertThat(recorder.testSuccessfulCount, equalTo(3))

    }

    @Test
    fun tesIncludeSubjectNotConfigured() {
        class Bar: Foo()
        class SubjectSpec: SubjectSpek<Foo>({
            test("this should fail") {
                subject.bar()
            }
        })

        class IncludedSubjectSpec: SubjectSpek<Bar>({
            includeSubjectSpec(SubjectSpec::class)
        })

        val recorder = executeTestsForClass(IncludedSubjectSpec::class)

        assertThat(recorder.testFailureCount, equalTo(1))
        val throwable = recorder.getFailingTestEvents().first().result.throwable
        fun isSpekException(throwable: Throwable) = throwable is SpekException
        assertThat(throwable.get(), ::isSpekException)
    }
}
