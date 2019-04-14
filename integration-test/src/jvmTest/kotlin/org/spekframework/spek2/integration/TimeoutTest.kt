package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.meta.Ignore

// remove @Ignore to test if timeouts are working.
@Ignore
object TimeoutTest: Spek({
    test("default timeout") {
        while (true) {}
    }

    test("custom timeout", timeout = 2000) {
        while (true) {}
    }
})
