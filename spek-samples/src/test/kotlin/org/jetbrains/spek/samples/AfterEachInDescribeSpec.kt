package org.jetbrains.spek.samples

import org.jetbrains.spek.Spek
import org.jetbrains.spek.dsl.describe
import org.jetbrains.spek.dsl.it
import kotlin.test.assertEquals

class AfterEachInDescribeSpec: Spek({

    describe("afterEach in describe block") {
        afterEach {
            globalX = 7
        }

        it("should execute before the first afterEach") {
            assertEquals(10, globalX)
            assertEquals(10, globalY)
            assertEquals(10, globalZ)
        }

        it("should execute after the afterEach") {
            assertEquals(7, globalX)
            assertEquals(7, globalY)
            assertEquals(10, globalZ)
        }

        describe("afterEach in nested describe block runs only for tests inside the nested describe") {
            afterEach {
                globalZ--
            }

            it("should execute before the first afterEach") {
                assertEquals(7, globalX)
                assertEquals(7, globalY)
                assertEquals(10, globalZ)
            }

            it("should execute after the afterEach") {
                assertEquals(7, globalX)
                assertEquals(7, globalY)
                assertEquals(9, globalZ)
            }
        }

        afterEach {
            globalY = 7
        }
    }
}) {
    companion object {
        var globalX = 10
        var globalY = 10
        var globalZ = 10
    }
}


