package samples

import spek.spec
import spek.given
import spek.on
import spek.it
import spek.shouldEqual


spec public fun calculatorSpecs() {


    given("a calculator", {
        val calculator = Calculator()
        on("calling sum with two numbers", {
            val sum = calculator.sum(2, 4)
            it("should return the result of adding the first number to the second", {
                shouldEqual(7, sum)
            })
        })
    })





}

class Calculator {

    fun sum(x: Int, y: Int) = x + y
}
