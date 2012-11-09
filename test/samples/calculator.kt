package samples

import spek.spec
import spek.given
import spek.on
import spek.it
import spek.shouldEqual
import spek.ygiven
import spek.xgiven


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

fun xtext()
{
    xgiven("sdfsdf")
    {
        val c = Calculator()
        xon("sdjfbsdjf")
        {
            val s = c.sum(7,5)
            xit("should")
            {
                shouldEqual(s, 12)
            }
        }

        xon("sdhfbsdhf")
        {

        }
    }
}


fun ytest()
{
    ygiven("Calculator")
    {
        Calculator()
    }.yon("calling sum with two number")
    {
        sum(2,4)
    }.yit("should")
    {
       shouldEqual(7)
    }
}

class Calculator {

    fun sum(x: Int, y: Int) = x + y
}
