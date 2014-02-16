package org.spek.samples


import org.junit.Test as test
import org.junit.runner.JUnitCore
import org.spek.console.*
import org.spek.*
import org.junit.internal.RealSystem
import org.junit.internal.TextListener


public class RunSamplesInJUnitTest {
    test fun try_junit() {
        with(JUnitCore()) {
            addListener(TextListener(RealSystem()))
            run(javaClass<IncUtilJUnitSpecs>(), javaClass<CalculatorJUnitSpecs>())
        }
    }

    test fun try_console() {
        org.spek.console.main(array(".", "org.spek.samples", "-f", "text"))
    }
}