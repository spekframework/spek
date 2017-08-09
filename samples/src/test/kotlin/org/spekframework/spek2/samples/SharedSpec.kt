package org.spekframework.spek2.samples

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.describe
import org.spekframework.spek2.dsl.it
import org.spekframework.spek2.include

object SharedSpec: Spek({
    describe("foo") {
        it("should be okay") {

        }
    }
})


class SomeSpek: Spek({
    include(SharedSpec)
})
