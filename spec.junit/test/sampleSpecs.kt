package org.spek.junit.test.samples

import org.spek.api.*
import org.spek.junit.api.*

public class calculatorSpecs : JUnitSpek() {{
    given("a calculator")
     {
         val calculator = JCalculator()
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

public class incUtilSpecs : JUnitSpek() {{
    given("an inc util") {
        val incUtil = JIncUtil()
        on("calling incVaueBy with 4 and given number 6") {
            val result = incUtil.incValueBy(4, 6)
            it("should return 10") {
                shouldEqual(result, 10)
            }
        }
    }
}}

class JIncUtil {
    fun incValueBy(value: Int, inc: Int) = value + inc
}

class JCalculator {
    fun sum(x: Int, y: Int) = x + y
    fun subtract(x: Int, y: Int) = x - y
}
