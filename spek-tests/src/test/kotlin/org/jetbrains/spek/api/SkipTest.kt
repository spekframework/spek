package org.jetbrains.spek.api

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
public class SkipTest {
    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun skipIt(runner: SpekTestCaseRunner) = runner.runTest({
        given("a situation") {
            on("an event") {
                it("should A") {
                    skip("not ready yet")
                }

                it("should B") { }
            }
        }
    }, """42 START
            given a situation START
            on an event START
            it should A START
            it should A SKIP
            it should A FINISH
            it should B START
            it should B FINISH
            on an event FINISH
            given a situation FINISH
            42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun skipOn(runner: SpekTestCaseRunner) = runner.runTest({
        given("a situation") {
            on("an event") {
                skip("not ready yet")

                it("should A") { }
            }

            on("another event") {

                it("should B") { }
            }
        }
    }, """42 START
            given a situation START
            on an event START
            on an event SKIP
            on an event FINISH
            on another event START
            it should B START
            it should B FINISH
            on another event FINISH
            given a situation FINISH
            42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun skipGiven(runner: SpekTestCaseRunner) = runner.runTest({
        given("a situation") {
            skip("for some reason")

            on("an event") {
                it("should A") { }
            }

            on("another event") {
                it("should B") {
                    pending("ok!!!")
                }
            }
        }
    }, """42 START
            given a situation START
            given a situation SKIP
            given a situation FINISH
            42 FINISH""")

    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun pendingIt(runner: SpekTestCaseRunner) = runner.runTest({
        given("a situation") {
            on("an event") {
                it("should A") {
                    pending("not implemented yet")
                }

                //default pending.
                it("should B")
            }
        }
    }, """42 START
            given a situation START
            on an event START
            it should A START
            it should A PEND
            it should A FINISH
            it should B START
            it should B PEND
            it should B FINISH
            on an event FINISH
            given a situation FINISH
            42 FINISH""")


    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun pendingOn(runner: SpekTestCaseRunner) = runner.runTest({
        given("a situation") {
            //default pending.
            on("an event")

            on("another event") {
                pending("not implemented yet")
            }
        }
    }, """42 START
            given a situation START
            on an event START
            on an event PEND
            on an event FINISH
            on another event START
            on another event PEND
            on another event FINISH
            given a situation FINISH
            42 FINISH""")


    @Test
    @Parameters(source = SpekTestCaseRunnerProvider::class)
    fun pendingGiven(runner: SpekTestCaseRunner) = runner.runTest({
        given("a situation") {
            pending("for some reason")
        }

        //default pending.
        given("another situation")
    }, """42 START
            given a situation START
            given a situation PEND
            given a situation FINISH
            given another situation START
            given another situation PEND
            given another situation FINISH
            42 FINISH""")
}