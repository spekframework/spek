package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import kotlin.test.fail

object SkipTest: Spek({
    group("skipped tests should not be executed") {
        test("skipped test", skip = Skip.Yes()) {
            fail()
        }

        test("non-skipped test") {}
    }

    group("skipped group", skip = Skip.Yes()) {
        test("failing test") {
            fail()
        }
    }
})
