package org.jetbrains.spek.api.integration

import org.junit.Test as test

class ExceptionHandlingTest : IntegrationTestCase() {
    @test fun exceptionThrownFromItBlock() = runTest(data{
        given("something") {
            on("something else") {
                it("should fail on exceptions") {
                    throw RuntimeException("error occurred")
                }
            }
        }
    }, """Spek: START
        given something: START
        on something else: START
        it should fail on exceptions: START
        it should fail on exceptions: FAIL: error occurred
        on something else: FINISH
        given something: FINISH
        Spek: FINISH""")

    @test fun exceptionThrownFromBeforeEach() = runTest(data{
        given("something") {
            beforeEach { throw RuntimeException("error occurred") }
            it("should fail") { }
        }
    }, """Spek: START
        given something: START
        it should fail: START
        it should fail: FAIL: error occurred
        given something: FINISH
        Spek: FINISH""")

    @test fun exceptionThrownFromAfterEach() = runTest(data{
        given("something") {
            afterEach { throw RuntimeException("error occurred") }
            it("should fail") { }
        }
    }, """Spek: START
        given something: START
        it should fail: START
        it should fail: FAIL: error occurred
        given something: FINISH
        Spek: FINISH""")
}