package org.jetbrains.spek.samples


import org.junit.Test as test
import org.junit.runner.JUnitCore
import org.junit.internal.RealSystem
import org.junit.internal.TextListener

class RunSamplesInJUnitTest {
    @test fun try_junit() {
        with(JUnitCore()) {
            addListener(TextListener(RealSystem()))
            run(SimpleTest::class.java, NestedDescribesTest::class.java)
        }
    }
}
