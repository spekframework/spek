package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.sameInstance
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

class MemoizedTest : AbstractSpekRuntimeTest() {
    @Test
    fun memoizedTestCaching() {
        class MemoizedSpec : Spek({
            val foo by memoized {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            test("first pass") {
                memoized1 = foo
            }

            test("second pass") {
                memoized2 = foo
            }

            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedTestCachingWithDestructor() {
        class MemoizedSpec : Spek({
            val foo by memoized(
                factory = { mutableListOf(1) },
                destructor = { it.clear() }
            )

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            test("first pass") {
                memoized1 = foo
            }

            test("second pass") {
                memoized2 = foo
            }

            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
                assertThat(memoized1, present(isEmpty))
                assertThat(memoized2, present(isEmpty))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }


    @Test
    fun memoizedTestBeforeAndAfterEachTest() {
        class MemoizedSpec : Spek({

            var counter = -1

            val foo by memoized {
                ++counter
            }

            beforeEachTest {
                assertThat(foo, equalTo(0))
            }

            test("check") {
                assertThat(foo, equalTo(0))
            }

            afterEachTest {
                assertThat(foo, equalTo(0))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)
        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedGroupCaching() {
        class MemoizedSpec : Spek({
            val foo by memoized(CachingMode.GROUP) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo
                }
            }

            group("another group") {
                test("second pass") {
                    memoized2 = foo
                }

            }


            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedGroupCachingWithDestructor() {
        class MemoizedSpec : Spek({
            val foo by memoized(
                mode = CachingMode.GROUP,
                factory = { mutableListOf(1) },
                destructor = { it.clear() }
            )

            var memoized: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized = foo
                }

                test("check") {
                    assertThat(memoized, present(!isEmpty))
                }
            }

            test("check") {
                assertThat(memoized, present(isEmpty))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedNestedGroupCaching() {
        class MemoizedSpec : Spek({
            val foo by memoized(CachingMode.GROUP) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo
                }

                group("another group") {
                    test("second pass") {
                        memoized2 = foo
                    }

                }
            }

            test("check") {
                assertThat(memoized1, !sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedNestedGroupCachingWithDestructor() {
        class MemoizedSpec : Spek({
            val foo by memoized(
                mode = CachingMode.GROUP,
                factory = { mutableListOf(1) },
                destructor = { it.clear() }
            )

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo
                }

                group("another group") {
                    test("second pass") {
                        memoized2 = foo
                    }
                }

                test("check") {
                    assertThat(memoized1, present(!isEmpty))
                    assertThat(memoized2, present(isEmpty))
                }
            }

            test("check") {
                assertThat(memoized1, present(isEmpty))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedScopeCaching() {
        class MemoizedSpec : Spek({
            val foo by memoized(CachingMode.SCOPE) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo
                }
            }

            group("another group") {
                test("second pass") {
                    memoized2 = foo
                }

            }


            test("check") {
                assertThat(memoized1, sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedScopeCachingWithNestedGroups() {
        class MemoizedSpec : Spek({
            val foo by memoized(CachingMode.SCOPE) {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            group("group") {
                test("first pass") {
                    memoized1 = foo
                }

                group("another group") {
                    test("second pass") {
                        memoized2 = foo
                    }

                }
            }

            test("check") {
                assertThat(memoized1, sameInstance(memoized2))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedScopeCachingWithDestructor() {
        class MemoizedSpec : Spek({
            var memoized: List<Int>? = null

            group("group") {
                val foo by memoized(
                    mode = CachingMode.SCOPE,
                    factory = { mutableListOf(1) },
                    destructor = { it.clear() }
                )

                group("another group") {
                    test("second pass") {
                        memoized = foo
                    }
                }

                test("check") {
                    assertThat(memoized, present(!isEmpty))
                }
            }

            test("check") {
                assertThat(memoized, present(isEmpty))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedActionCaching() {
        class MemoizedSpec : Spek({
            val foo by memoized {
                listOf(1)
            }

            var memoized1: List<Int>? = null
            var memoized2: List<Int>? = null

            action("some action") {
                test("first pass") {
                    memoized1 = foo
                }

                test("second pass") {
                    memoized2 = foo
                }

                test("check") {
                    assertThat(memoized1, sameInstance(memoized2))
                }
            }


        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedActionCachingWithDestructor() {
        class MemoizedSpec : Spek({
            val foo by memoized(
                factory = { mutableListOf(1) },
                destructor = { it.clear() }
            )

            var memoized: List<Int>? = null

            action("some action") {
                test("first pass") {
                    memoized = foo
                }

                test("check") {
                    assertThat(memoized, present(!isEmpty))
                }
            }

            test("check") {
                assertThat(memoized, present(isEmpty))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }

    @Test
    fun memoizedWithNullValue() {
        class MemoizedSpec : Spek({

            val foo by memoized { null }

            test("check") {
                assertThat(foo, equalTo(null))
            }
        })

        val recorder = executeTestsForClass(MemoizedSpec::class)

        assertThat(recorder.testFailureCount, equalTo(0))
    }
}
