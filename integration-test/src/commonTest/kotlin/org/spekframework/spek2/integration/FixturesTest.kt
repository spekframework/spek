package org.spekframework.spek2.integration

import org.spekframework.spek2.Spek
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object FixturesTest: Spek({
    group("before/after each test") {
        val list = mutableListOf<Int>()

        beforeEachTest {
            assertTrue(list.isEmpty())
            list.add(1)
        }

        group("some group") {
            beforeEachTest { list.add(2) }

            test("list should be of expected size") {
                assertEquals(2, list.size)
            }

            afterEachTest { list.remove(2) }
        }

        afterEachTest { list.remove(1) }
    }

    group("before/after group") {
        val list = mutableListOf<Int>()
        beforeGroup {
            list.add(1)
        }

        group("some group") {
            beforeGroup {
                list.add(2)
            }

            test("list should be of expected size") {
                assertEquals(2, list.size)
            }

            test("list should be of expected size") {
                assertEquals(2, list.size)
            }

            afterGroup {
                list.remove(2)
            }
        }

        afterGroup {
            list.remove(1)
            assertTrue(list.isEmpty())
        }
    }
})
