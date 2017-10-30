package org.spekframework.spek2.samples

import org.spekframework.spek2.Spek
import kotlin.test.assertEquals

class SkipSampleSpec : Spek({
    val result = 10

    it("should return 10") {
        assertEquals(result, 10)
    }

    xit("should not return 11") {
        // Waiting for next release
    }

    xdescribe("not implemented yet") {
        it("should return 10") {
            assertEquals(11, 10)
        }
    }

    xcontext("not implemented yet") {
        it("should return 10") {
            assertEquals(11, 10)
        }
    }

    xgiven("not implemented yet") {
        it("should return 10") {
            assertEquals(11, 10)
        }
    }

    xon("not implemented yet") {
        it("should return 10") {
            assertEquals(11, 10)
        }
    }
})

