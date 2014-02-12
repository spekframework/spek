package org.spek.samples.s1

import org.spek.console.*
import org.spek.*

fun Specification.this_is_test() {
    given("Outside it is raining") {
        on("umbrella") {
            it("should not be raining") {
                shouldBeTrue(true)
            }
            it("should not be sunny") {
                shouldBeTrue(false)
            }
        }
    }
}


class calculatorSpecs : Spek() {{
    given("a calculator")
     {
         val calculator = Calculator()
         on("calling sum with two numbers")
         {

             val sum = calculator.sum(2, 4)


             it("should return the result of adding the first number to the second number")
             {
                 shouldEqual(6, sum)
             }

             it("should another")
             {
                 shouldEqual(6, sum)
             }
         }

         on("calling substract with two numbers")
         {
             val subtract = calculator.subtract(4, 2)

             it("should return the result of substracting the second number from the first number")
             {

                 shouldEqual(2, subtract)
             }
         }
     }
}}

class incUtilSpecs : Spek() {{
    given("an inc util") {
        val incUtil = IncUtil()
        on("calling incVaueBy with 4 and given number 6") {
            val result = incUtil.incValueBy(4, 6)
            it("should return 10") {
                shouldEqual(result, 10)
            }
        }
    }
}}

class IncUtil {
    fun incValueBy(value: Int, inc: Int) = value + inc
}

class Calculator {
    fun sum(x: Int, y: Int) = x + y
    fun subtract(x: Int, y: Int) = x - y
}
