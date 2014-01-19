package org.spek.samples


import org.junit.Test as test
import org.junit.runner.JUnitCore
import org.spek.console.api.ConsoleSpek
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
        //TODO: org.Reflections detects no test classes if "org.spek.samples" is specified
        org.spek.console.cmd.main("org.spek", "-text")
    }
}