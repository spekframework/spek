package org.jetbrains.spek.api

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
        an inc util: START
        calling incValueBy with 4 and given number 6: START
        it should return 10: START
        it should return 10: FINISH
        calling incValueBy with 4 and given number 6: FINISH
        an inc util: FINISH
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
                a calculator: START
                calling sum with two numbers: START
                it should return the result of adding the first number to the second number: START
                it should return the result of adding the first number to the second number: FINISH
                calling sum with two numbers: FINISH
                a calculator: FINISH
                Spek: FINISH
                Spek: START
                a calculator: START
                calling sum with two numbers: START
                it should another: START
                it should another: FINISH
                calling sum with two numbers: FINISH
                a calculator: FINISH
                Spek: FINISH
                Spek: START
                a calculator: START
                calling subtract with two numbers: START
                it should return the result of subtracting the second number from the first number: START
                it should return the result of subtracting the second number from the first number: FINISH
                calling subtract with two numbers: FINISH
                a calculator: FINISH
                Spek: FINISH""")

}
