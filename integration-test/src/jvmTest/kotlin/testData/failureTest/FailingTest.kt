package testData.failureTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object FailingTest : Spek({
    describe("equality") {
        it("4 == 2 + 2") {
            assertEquals(4, 2 + 2)
        }
        it("false == true") {
            assertEquals(false, true)
        }
    }
    describe("empty description") {
        it("") {
            assertEquals(2, 2)
        }
        it("") {
            assertEquals(2, 2)
        }
    }
    describe("") {
        it("4 == 2 + 2") {
            assertEquals(4, 2 + 2)
        }
    }
})
