package org.spek.samples

import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.console.listeners.text.PlainTextListener
import org.spek.console.output.console.ConsoleDevice

class SampleCalculatorTest {
    test fun calculate() {
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(ConsoleDevice()))

        val givenActions = CalculatorConsoleSpecs().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }
    }
}


class CalculatorConsoleSpecs: ConsoleSpek() {{
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

