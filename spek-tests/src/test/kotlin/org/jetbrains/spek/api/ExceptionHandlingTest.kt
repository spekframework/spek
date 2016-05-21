package org.jetbrains.spek.api

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
        something: START
        something else: START
        it should fail on exceptions: START
        it should fail on exceptions: FAIL: error occurred
        something else: FINISH
        something: FINISH
        Spek: FINISH""")

    @test fun exceptionThrownFromBeforeEach() = runTest(data{
        given("something") {
            beforeEach { throw RuntimeException("error occurred") }
            it("should fail") { }
        }
    }, """Spek: START
        something: START
        it should fail: START
        it should fail: FAIL: error occurred
        something: FINISH
        Spek: FINISH""")

    @test fun exceptionThrownFromAfterEach() = runTest(data{
        given("something") {
            afterEach { throw RuntimeException("error occurred") }
            it("should fail") { }
        }
    }, """Spek: START
        something: START
        it should fail: START
        it should fail: FAIL: error occurred
        something: FINISH
        Spek: FINISH""")
}