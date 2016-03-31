package org.jetbrains.spek.api

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpekTreeTest : Spek({
    fun focusedIt(): SpekTree {
        return SpekTree(
                "fit",
                ActionType.IT,
                SpekIgnoreRunner(),
                listOf(),
                true
        )
    }

    fun unfocusedIt(): SpekTree {
        return SpekTree(
                "it",
                ActionType.IT,
                SpekIgnoreRunner(),
                listOf(),
                false
        )
    }

    fun focusedDescribe(vararg children: SpekTree): SpekTree {
        return SpekTree(
                "fdescribe",
                ActionType.DESCRIBE,
                SpekIgnoreRunner(),
                children.toList(),
                true
        )
    }

    fun unfocusedDescribe(vararg children: SpekTree): SpekTree {
        return SpekTree(
                "describe",
                ActionType.DESCRIBE,
                SpekIgnoreRunner(),
                children.toList(),
                false
        )
    }

    fun focusedPath(vararg indices: Int): Path {
        return Path(indices.toList(), true)
    }

    fun unfocusedPath(vararg indices: Int): Path {
        return Path(indices.toList(), false)
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
            assertTrue(focusedDescribe(unfocusedIt()).focused())
        }

        it("should return true for a describe that has a focused child") {
            val subject = unfocusedDescribe(focusedIt())
            assertTrue(subject.focused())
        }

        it("should return false for a describe with no focus") {
            assertFalse(unfocusedDescribe(unfocusedIt()).focused())
        }
    }

    describe("getPaths") {
        it("should return all the paths with the focused flag set to true or false appropriately") {
            val subject = unfocusedDescribe(
                    unfocusedIt(),
                    unfocusedDescribe(focusedIt(), unfocusedIt()),
                    unfocusedDescribe(unfocusedIt()),
                    focusedDescribe(unfocusedIt(), unfocusedIt()),
                    focusedDescribe(unfocusedDescribe(unfocusedIt()))
            )

            assertEquals(subject.getPaths(),
                    setOf(
                            unfocusedPath(0),
                            focusedPath(1, 0), unfocusedPath(1, 1),
                            unfocusedPath(2,0),
                            focusedPath(3, 0), focusedPath(3, 1),
                            focusedPath(4,0,0)
                    ))
        }

        it("return paths with the focused flag set to false if none of the nodes are focused") {
            val subject = unfocusedDescribe(
                    unfocusedIt(),
                    unfocusedDescribe(unfocusedIt())
            )

            assertEquals(subject.getPaths(), setOf(
                    unfocusedPath(0),
                    unfocusedPath(1, 0)
            ))
        }
    }
})