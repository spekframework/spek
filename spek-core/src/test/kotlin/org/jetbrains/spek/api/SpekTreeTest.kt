package org.jetbrains.spek.api

import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpekTreeTest : Spek({
    fun focusedIt() : SpekTree {
        return SpekTree(
                "fit",
                ActionType.IT,
                SpekIgnoreRunner(),
                listOf(),
                true
        )
    }

    fun unfocusedIt() : SpekTree {
        return SpekTree(
                "it",
                ActionType.IT,
                SpekIgnoreRunner(),
                listOf(),
                false
        )
    }

    fun focusedDescribe(children: List<SpekTree>): SpekTree {
        return SpekTree(
                "fdescribe",
                ActionType.DESCRIBE,
                SpekIgnoreRunner(),
                children,
                true
        )
    }

    fun unfocusedDescribe(children: List<SpekTree>): SpekTree {
        return SpekTree(
                "describe",
                ActionType.DESCRIBE,
                SpekIgnoreRunner(),
                children,
                false
        )
    }

    describe("focused") {
        it("should return true for a focused it") {
            val subject = focusedIt()
            assertTrue(subject.focused())
        }

        it("should return false for an unfocused it") {
            assertFalse(unfocusedIt().focused())
        }

        it("should return true for a focused describe") {
            assertTrue(focusedDescribe(listOf(unfocusedIt())).focused())
        }

        it("should return true for a describe that has a focused child") {
            val subject = unfocusedDescribe(listOf(focusedIt()))
            assertTrue(subject.focused())
        }

        it("should return false for a describe with no focus") {
            assertFalse(unfocusedDescribe(listOf(unfocusedIt())).focused())
        }
    }
})