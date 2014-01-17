package org.spek.impl

import org.junit.Test as test
import org.spek.impl.IntegrationTestCase.Data
import kotlin.test.fail
import org.junit.Assert

public class SampleCalculatorIntegrationTest : IntegrationTestCase() {
    test fun inc() = runTest(data{
        class SampleIncUtil {
            fun incValueBy(value: Int, inc: Int) = value + inc
        }

        given("an inc util") {
            val incUtil = SampleIncUtil()
            on("calling incVaueBy with 4 and given number 6") {
                val result = incUtil.incValueBy(4, 6)
                it("should return 10") {
                    shouldEqual(result, 10)
                }
            }
        }
    }, """SPEK:42 START
        GIVEN:given an inc util START
        GIVEN:given an inc util ON:on calling incVaueBy with 4 and given number 6 START
        GIVEN:given an inc util ON:on calling incVaueBy with 4 and given number 6 IT:it should return 10 START
        GIVEN:given an inc util ON:on calling incVaueBy with 4 and given number 6 IT:it should return 10 FINISH
        GIVEN:given an inc util ON:on calling incVaueBy with 4 and given number 6 FINISH
        GIVEN:given an inc util FINISH
        SPEK:42 FINISH""")

    test fun calculate() = runTest(data{
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
        },   """SPEK:42 START
                GIVEN:given a calculator START
                GIVEN:given a calculator ON:on calling sum with two numbers START
                GIVEN:given a calculator ON:on calling sum with two numbers IT:it should return the result of adding the first number to the second number START
                GIVEN:given a calculator ON:on calling sum with two numbers IT:it should return the result of adding the first number to the second number FINISH
                GIVEN:given a calculator ON:on calling sum with two numbers IT:it should another START
                GIVEN:given a calculator ON:on calling sum with two numbers IT:it should another FINISH
                GIVEN:given a calculator ON:on calling sum with two numbers FINISH
                GIVEN:given a calculator ON:on calling substract with two numbers START
                GIVEN:given a calculator ON:on calling substract with two numbers IT:it should return the result of substracting the second number from the first number START
                GIVEN:given a calculator ON:on calling substract with two numbers IT:it should return the result of substracting the second number from the first number FINISH
                GIVEN:given a calculator ON:on calling substract with two numbers FINISH
                GIVEN:given a calculator FINISH
                SPEK:42 FINISH""")

}