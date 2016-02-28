package org.jetbrains.spek.api

import org.junit.Test as test

class SampleCalculatorIntegrationTest : IntegrationTestCase() {
    @test fun inc() = runTest(data {
        class SampleIncUtil {
            fun incValueBy(value: Int, inc: Int) = value + inc
        }

        given("an inc util") {
            val incUtil = SampleIncUtil()
            on("calling incValueBy with 4 and given number 6") {
                val result = incUtil.incValueBy(4, 6)
                it("should return 10") {
                    shouldEqual(result, 10)
                }
            }
        }
    }, """SPEK: 42 START
        SPEK: 42 GIVEN: given an inc util START
        SPEK: 42 GIVEN: given an inc util ON: on calling incValueBy with 4 and given number 6 START
        SPEK: 42 GIVEN: given an inc util ON: on calling incValueBy with 4 and given number 6 IT: it should return 10 START
        SPEK: 42 GIVEN: given an inc util ON: on calling incValueBy with 4 and given number 6 IT: it should return 10 FINISH
        SPEK: 42 GIVEN: given an inc util ON: on calling incValueBy with 4 and given number 6 FINISH
        SPEK: 42 GIVEN: given an inc util FINISH
        SPEK: 42 FINISH""")

    @test fun calculate() = runTest(data {
        class SampleCalculator {
            fun sum(x: Int, y: Int) = x + y
            fun subtract(x: Int, y: Int) = x - y
        }
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
    }, """SPEK: 42 START
                SPEK: 42 GIVEN: given a calculator START
                SPEK: 42 GIVEN: given a calculator ON: on calling sum with two numbers START
                SPEK: 42 GIVEN: given a calculator ON: on calling sum with two numbers IT: it should return the result of adding the first number to the second number START
                SPEK: 42 GIVEN: given a calculator ON: on calling sum with two numbers IT: it should return the result of adding the first number to the second number FINISH
                SPEK: 42 GIVEN: given a calculator ON: on calling sum with two numbers IT: it should another START
                SPEK: 42 GIVEN: given a calculator ON: on calling sum with two numbers IT: it should another FINISH
                SPEK: 42 GIVEN: given a calculator ON: on calling sum with two numbers FINISH
                SPEK: 42 GIVEN: given a calculator ON: on calling substract with two numbers START
                SPEK: 42 GIVEN: given a calculator ON: on calling substract with two numbers IT: it should return the result of substracting the second number from the first number START
                SPEK: 42 GIVEN: given a calculator ON: on calling substract with two numbers IT: it should return the result of substracting the second number from the first number FINISH
                SPEK: 42 GIVEN: given a calculator ON: on calling substract with two numbers FINISH
                SPEK: 42 GIVEN: given a calculator FINISH
                SPEK: 42 FINISH""")

}
