package org.jetbrains.spek.api

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class SampleTests {

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun inc(runner: SpekTestCaseRunner) = runner.runTest({
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
    }, """42 START
                given an inc util START
                on calling incValueBy with 4 and given number 6 START
                it should return 10 START
                it should return 10 FINISH
                on calling incValueBy with 4 and given number 6 FINISH
                given an inc util FINISH
                42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun calculate(runner: SpekTestCaseRunner) = runner.runTest({
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
    }, """42 START
                given a calculator START
                on calling sum with two numbers START
                it should return the result of adding the first number to the second number START
                it should return the result of adding the first number to the second number FINISH
                it should another START
                it should another FINISH
                on calling sum with two numbers FINISH
                on calling substract with two numbers START
                it should return the result of substracting the second number from the first number START
                it should return the result of substracting the second number from the first number FINISH
                on calling substract with two numbers FINISH
                given a calculator FINISH
                42 FINISH""")
}
