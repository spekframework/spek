package org.jetbrains.spek.api

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PathTest : Spek({
    describe("append") {
        it("appends a an index to the indexPath") {
            val result = Path(listOf(), false).append(4, false).append(3, true).append(0, false)
            assertEquals(result.indexPath, listOf(0,3,4))
        }

        context("with an unfocused path") {
            val subject = Path(listOf(), false)

            it("returns an unfocused path when appending an unfocused node") {
                val result = subject.append(4, false)
                assertFalse(result.focused)
            }

            it("returns a focused path when appending a focused node") {
                val result = subject.append(1, true)
                assertTrue(result.focused)
            }
        }

        context("with a focused path") {
            val subject = Path(listOf(), true)

            it("returns a focused path when appending an unfocused node") {
                val result = subject.append(4, false)
                assertTrue(result.focused)
            }

            it("returns a focused path when appending a focused node") {
                val result = subject.append(1, true)
                assertTrue(result.focused)
            }
        }
    }

    describe("subPath") {
        context("with an unfocused path") {
            val subject = Path(listOf(2,6,3), false)
            it("returns an unfocused path with the first index stripped off") {
                assertEquals(subject.subPath(), Path(listOf(6,3), false))
            }
        }

        context("with a focused path") {
            val subject = Path(listOf(2,6,3), true)
            it("returns a focused path with the first index stripped off") {
                assertEquals(subject.subPath(), Path(listOf(6,3), true))
            }
        }
    }
})