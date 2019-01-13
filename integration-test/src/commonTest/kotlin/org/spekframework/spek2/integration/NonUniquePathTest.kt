package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode
import kotlin.test.assertEquals

object NonUniquePathTest: Spek({
    group("duplicate description is allowed") {
        val list by memoized(CachingMode.SCOPE) { mutableListOf<Int>() }

        test("duplicate test") {
            list.add(1)
        }

        test("duplicate test") {
            list.add(2)
        }

        afterGroup {
            assertEquals(2, list.size)
        }
    }
})
