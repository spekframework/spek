package org.spekframework.spek2.specification

import kotlin.test.assertTrue

object SampleSpecification: Spek({
    describe("a suite") {
        it("should pass") {
            assertTrue(true)
        }

        describe("a nested suite") {
            it("should also pass") {
                assertTrue(true)
            }
        }
    }
})
