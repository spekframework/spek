package org.jetbrains.spek.samples

import org.jetbrains.spek.Spek
import org.jetbrains.spek.dsl.it
import kotlin.test.assertEquals

class AfterEachSpec: Spek({
    afterEach {
        globalX = 7
    }

    it("should execute before the first afterEach") {
        assertEquals(10, globalX)
        assertEquals(10, globalY)
    }

    it("should execute after the afterEach") {
        assertEquals(7, globalX)
        assertEquals(7, globalY)
    }

    afterEach {
        globalY = 7
    }
}) {
    companion object {
        var globalX = 10
        var globalY = 10
    }
}


