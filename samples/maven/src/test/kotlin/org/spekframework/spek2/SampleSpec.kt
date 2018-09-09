package org.spekframework.spek2

import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue

object SampleSpec: Spek({
    describe("Something") {
        it("should do this") {
            assertTrue(true)
        }
    }
})
