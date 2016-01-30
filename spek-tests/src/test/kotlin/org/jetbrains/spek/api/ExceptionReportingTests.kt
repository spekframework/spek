package org.jetbrains.spek.api

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by jakub on 30/01/16.
 */


@RunWith(JUnitParamsRunner::class)
public class ExceptionReportingTests {

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun beforeEachThrows(runner: SpekTestCaseRunner) = runner.runTest({
        beforeEach { throw RuntimeException("ups") }
        given("1") {
            on("2") {
                it("3") {}
            }
        }
    }, """42 START
            given 1 START
            on 2 START
            on 2 FAIL: ups
            on 2 FINISH
            given 1 FINISH
            42 FINISH""")


    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun afterEachThrows(runner: SpekTestCaseRunner) = runner.runTest({
        afterEach { throw RuntimeException("ups") }
        given("1") {
            on("2") {
                it("3") {}
            }
        }
    }, """42 START
            given 1 START
            on 2 START
            it 3 START
            it 3 FINISH
            on 2 FAIL: ups
            on 2 FINISH
            given 1 FINISH
            42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun beforeOnThrows(runner: SpekTestCaseRunner) = runner.runTest({
        given("1") {
            beforeOn { throw RuntimeException("ups") }
            on("2") {
                it("3") {}
            }
        }
    }, """42 START
            given 1 START
            on 2 START
            on 2 FAIL: ups
            on 2 FINISH
            given 1 FINISH
            42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun afterOnThrows(runner: SpekTestCaseRunner) = runner.runTest({
        given("1") {
            afterOn { throw RuntimeException("ups") }
            on("2") {
                it("3") {}
            }
        }
    }, """42 START
            given 1 START
            on 2 START
            it 3 START
            it 3 FINISH
            on 2 FAIL: ups
            on 2 FINISH
            given 1 FINISH
            42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun givenExpressionThrows(runner: SpekTestCaseRunner) = runner.runTest({
        given("1") {
            throw RuntimeException("ups")
            on("2") {
                it("3") {}
            }
        }
    }, """42 START
            given 1 START
            given 1 FAIL: ups
            given 1 FINISH
            42 FINISH""")


    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun onExpressionThrows(runner: SpekTestCaseRunner) = runner.runTest({
        given("1") {
            on("2") {
                throw RuntimeException("ups")
                it("3") {}
            }
        }
    }, """42 START
            given 1 START
            on 2 START
            on 2 FAIL: ups
            on 2 FINISH
            given 1 FINISH
            42 FINISH""")


    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun itExpressionThrows(runner: SpekTestCaseRunner) = runner.runTest({
        given("1") {
            on("2") {
                it("3") { throw RuntimeException("ups")}
            }
        }
    }, """42 START
            given 1 START
            on 2 START
            it 3 START
            it 3 FAIL: ups
            it 3 FINISH
            on 2 FINISH
            given 1 FINISH
            42 FINISH""")
}