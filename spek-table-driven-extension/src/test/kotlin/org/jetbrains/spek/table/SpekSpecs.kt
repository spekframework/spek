package org.jetbrains.spek.table

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware
import org.jetbrains.spek.api.lifecycle.LifecycleListener

class SpekSpecs : Spek({
    describe("Spek table driven extension") {
        given("a spec container") {
            val countingTestContainer: CountingTestContainer = CountingTestContainer()
            beforeEachTest {
                countingTestContainer.reset()
            }
            it("adds test cases as table data using the unroll function") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1),
                            testCase(2)
                    ) {
                        it("should be a visible test and use $it") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(2)
            }
            it("accepts any type in the table data") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase("1" as Any)
                    ) {
                        it("should be visible with $it") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(1)
            }
            it("accepts table data of length 2") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2)
                    ) { a, b ->
                        it("should be visible with $a $b") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(1)
            }
            it("keeps strong typing within the tests created by unroll") {
                var typeKept = false
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, "2")
                    ) { a, b ->
                        typeKept = a is Int && b is String
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                typeKept shouldMatch equalTo(true)
            }
            it("accepts table data of length 3") {
                val unroll: Spek = wrap {
                    unroll(
                            testCase(1, 2, 3)
                    ) { a, b, c ->
                        it("should be visible with $a $b $c") {}
                    }
                }
                unroll.spec.invoke(countingTestContainer)
                countingTestContainer.testNumber shouldMatch equalTo(1)
            }
        }
    }
})


private class CountingTestContainer : TestContainer, Spec {
    override fun beforeGroup(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterGroup(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun group(description: String, pending: Pending, body: SpecBody.() -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun action(description: String, pending: Pending, body: ActionBody.() -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> memoized(mode: CachingMode, factory: () -> T): LifecycleAware<T> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerListener(listener: LifecycleListener) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun test(description: String, pending: Pending, body: TestBody.() -> Unit) {
        testNumber++
    }

    var testNumber: Int = 0

    fun reset() {
        testNumber = 0
    }


}