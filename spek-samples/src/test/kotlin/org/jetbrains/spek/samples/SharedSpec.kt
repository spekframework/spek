package org.jetbrains.spek.samples

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.include

object SharedSpec: Spek({
    describe("foo") {
        it("should be okay") {

        }
    }
})


class SomeSpek: Spek({
    include(SharedSpec)
})
