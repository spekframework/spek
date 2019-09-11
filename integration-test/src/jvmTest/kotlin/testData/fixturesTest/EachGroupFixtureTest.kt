package testData.fixturesTest

import org.spekframework.spek2.Spek
import kotlin.test.assertEquals

object EachGroupFixtureTest: Spek({
    group("before/after each group") {
        val list = mutableListOf<Int>()

        test("list size should be 0") {
            assertEquals(0, list.size)
        }

        group("some group") {
            beforeEachGroup {
                list.add(2)
            }

            test("list size should be 1") {
                assertEquals(1, list.size)
            }

            group("another group #1") {
                test("list size should be 2") {
                    assertEquals(2, list.size)
                }
            }

            group("another group #2") {
                group("another group #3") {
                    test("list size should be 3") {
                        assertEquals(3, list.size)
                    }
                }
            }

            afterEachGroup {
                list.remove(2)
            }
        }
    }
})