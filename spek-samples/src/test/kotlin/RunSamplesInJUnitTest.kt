package org.jetbrains.spek.samples


import org.junit.Test as test
import org.junit.runner.JUnitCore
import org.junit.internal.RealSystem
import org.junit.internal.TextListener
import org.jetbrains.spek.console.main


public class RunSamplesInJUnitTest {
    @test fun try_junit() {
        with(JUnitCore()) {
            addListener(TextListener(RealSystem()))
            run(IncUtilJUnitSpecs::class.java, CalculatorJUnitSpecs::class.java)
        }
    }

    @test fun try_console() {
        main(arrayOf(".", "org.jetbrains.spek.samples", "-f", "text"))
    }
}
