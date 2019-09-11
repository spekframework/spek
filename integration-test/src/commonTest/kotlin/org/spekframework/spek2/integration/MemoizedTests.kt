package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

object MemoizedTests: Spek({
    group("by default each tests will have a unique instance") {
        lateinit var previousValue: Set<String>
        val value by memoized { setOf("") }

        test("previous value") {
            previousValue = value
        }

        test("should not be equal to previous value") {
            assertTrue(previousValue !== value)
        }
    }

    group("Using CachingMode.SCOPE, every test within the same group will share the same instance") {
        lateinit var previousValue: Set<String>
        val value by memoized(CachingMode.SCOPE) { setOf("") }

        test("previous value") {
            previousValue = value
        }

        test("should be equal to previous value") {
            assertTrue(previousValue === value)
        }

        test("should be equal to previous value") {
            assertTrue(previousValue === value)
        }
    }

    group("Using CachingMode.EACH_GROUP, each group will have a unique instance") {
        lateinit var previousValue: Set<String>
        val value by memoized(CachingMode.EACH_GROUP) { setOf("") }

        group("first") {
            beforeGroup {
                previousValue = value
            }

            test("should be equal to previous value") {
                assertSame(previousValue, value)
            }

            test("should be equal to previous value") {
                assertSame(previousValue, value)
            }
        }

        group("second") {
            test("should not be equal to previous value") {
                assertNotSame(previousValue, value)
            }
        }
    }
})
