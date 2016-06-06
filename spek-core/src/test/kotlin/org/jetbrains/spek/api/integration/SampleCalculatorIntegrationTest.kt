package org.jetbrains.spek.api.integration

import kotlin.test.assertEquals
import org.junit.Test as test

class SampleCalculatorIntegrationTest : IntegrationTestCase() {
    @test fun inc() = runTest(data{
        class SampleIncUtil {
            fun incValueBy(value: Int, inc: Int) = value + inc
        }

        given("an inc util") {
            val incUtil = SampleIncUtil()
            on("calling incValueBy with 4 and given number 6") {
                val result = incUtil.incValueBy(4, 6)
                it("should return 10") {
                    assertEquals(result, 10)
                }
            }
        }
    }, """Spek: START
        given an inc util: START
        on calling incValueBy with 4 and given number 6: START
        it should return 10: START
        it should return 10: FINISH
        on calling incValueBy with 4 and given number 6: FINISH
        given an inc util: FINISH
        Spek: FINISH""")

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
                        assertEquals(6, sum)
                    }

                    it("should another") {
                        assertEquals(6, sum)
                    }
                }

                on("calling subtract with two numbers") {
                    val subtract = calculator.subtract(4, 2)

                    it("should return the result of subtracting the second number from the first number") {

                        assertEquals(2, subtract)
                    }
                }
            }
        },   """Spek: START
                given a calculator: START
                on calling sum with two numbers: START
                it should return the result of adding the first number to the second number: START
                it should return the result of adding the first number to the second number: FINISH
                on calling sum with two numbers: FINISH
                given a calculator: FINISH
                Spek: FINISH

                Spek: START
                given a calculator: START
                on calling sum with two numbers: START
                it should another: START
                it should another: FINISH
                on calling sum with two numbers: FINISH
                given a calculator: FINISH
                Spek: FINISH

                Spek: START
                given a calculator: START
                on calling subtract with two numbers: START
                it should return the result of subtracting the second number from the first number: START
                it should return the result of subtracting the second number from the first number: FINISH
                on calling subtract with two numbers: FINISH
                given a calculator: FINISH
                Spek: FINISH""")

}
