package org.spekframework.speksample

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object HelloSpec: Spek({
    describe("A Hello") {
        it("should say hello") {
            assertEquals(expected = "Hello", actual = Hello().hello())
        }
    }

})
