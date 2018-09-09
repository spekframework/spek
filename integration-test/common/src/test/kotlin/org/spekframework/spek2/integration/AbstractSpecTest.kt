package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import kotlin.test.fail

abstract class AbstractSpecTest: Spek({
    test("should not be executed") {
        fail("expected to be ignored")
    }
})
