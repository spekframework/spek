package org.jetbrains.spek.samples


import org.junit.internal.RealSystem
import org.junit.internal.TextListener
import org.junit.runner.JUnitCore
import org.junit.Test as test

class RunSamplesInJUnitTest {
    @test fun try_junit() {
        with(JUnitCore()) {
            addListener(TextListener(RealSystem()))
            run(SimpleTest::class.java, NestedDescribesTest::class.java)
        }
    }
}
