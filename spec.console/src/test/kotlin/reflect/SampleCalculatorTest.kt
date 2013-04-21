package org.spek.console.reflect;

import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.console.listeners.text.PlainTextListener
import kotlin.test.assertEquals

class SampleCalculatorTest {
    test fun calculate() {
        val buffer = StringBuilder()
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SampleCalculatorSpecs().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expected(), buffer.toString())
    }


    private fun expected(): String {
        return "Given given a calculator" +
        "On calling sum with two numbers" +
        "It should return the result of adding the first number to the second number" +
        "It should another" +
        "On calling substract with two numbers" +
        "It should return the result of substracting the second number from the first number"
    }
}


class SampleCalculatorSpecs: ConsoleSpek() {{
    given("a calculator") {
        val calculator = SampleCalculator()
        on("calling sum with two numbers") {

            val sum = calculator.sum(2, 4)


            it("should return the result of adding the first number to the second number") {
                shouldEqual(6, sum)
            }

            it("should another") {
                shouldEqual(6, sum)
            }
        }

        on("calling substract with two numbers") {
            val subtract = calculator.subtract(4, 2)

            it("should return the result of substracting the second number from the first number") {

                shouldEqual(2, subtract)
            }
        }
    }
}
}

class SampleCalculator {
    fun sum(x: Int, y: Int) = x + y
    fun subtract(x: Int, y: Int) = x - y
}
