package org.jetbrains.spek.api

import org.mockito.Mockito.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpekTreeTest : Spek({
    describe("containsFocusedNodes") {
        it("should return true for a focused it") {
            val subject = focusedIt()
            assertTrue(subject.containsFocusedNodes())
        }

        it("should return false for an unfocused it") {
            assertFalse(unfocusedIt().containsFocusedNodes())
        }

        it("should return true for a focused describe") {
            assertTrue(focusedDescribe(unfocusedIt()).containsFocusedNodes())
        }

        it("should return true for a describe that has a focused child") {
            val subject = unfocusedDescribe(focusedIt())
            assertTrue(subject.containsFocusedNodes())
        }

        it("should return false for a describe with no focus") {
            assertFalse(unfocusedDescribe(unfocusedIt()).containsFocusedNodes())
        }
    }

    describe("run") {
        context("with no focused nodes") {
            it("should run all the it blocks in the spek") {
                val unfocusedIt1 = unfocusedIt()
                val unfocusedIt2 = unfocusedIt()

                val subject = unfocusedDescribe(
                        unfocusedIt1,
                        unfocusedDescribe(unfocusedIt2)
                )
                val mockNotifier = mock(Notifier::class.java)

                subject.run(mockNotifier)
                verify(mockNotifier).start(unfocusedIt1)
                verify(mockNotifier).start(unfocusedIt2)
            }
        }

        context("with some focused nodes") {
            it("should only run it blocks that are focused or have a focused parent") {
                val focusedIt = focusedIt()
                val unfocusedIt1 = unfocusedIt()
                val unfocusedIt2 = unfocusedIt()
                val unfocusedIt3 = unfocusedIt()
                val unfocusedIt4 = unfocusedIt()
                val unfocusedIt5 = unfocusedIt()
                val unfocusedIt6 = unfocusedIt()

                val subject = unfocusedDescribe(
                        unfocusedIt1,
                        unfocusedDescribe(
                                focusedIt,
                                unfocusedIt2
                        ),
                        unfocusedDescribe(
                                unfocusedIt3
                        ),
                        focusedDescribe(
                                unfocusedIt4,
                                unfocusedIt5
                        ),
                        unfocusedDescribe(
                                focusedDescribe(
                                        unfocusedDescribe(unfocusedIt6)
                                )
                        )
                )

                val mockNotifier = mock(Notifier::class.java)

                subject.run(mockNotifier)

                verify(mockNotifier).start(focusedIt)
                verify(mockNotifier).start(unfocusedIt4)
                verify(mockNotifier).start(unfocusedIt5)
                verify(mockNotifier).start(unfocusedIt6)

                verify(mockNotifier, never()).start(unfocusedIt1)
                verify(mockNotifier, never()).start(unfocusedIt2)
                verify(mockNotifier, never()).start(unfocusedIt3)
            }
        }
    }
}) {
    companion object {
        fun focusedIt(): SpekTree {
            return SpekTree(
                    "fit",
                    ActionType.IT,
                    fakeSpekNodeRunner(),
                    listOf(),
                    true
            )
        }

        fun unfocusedIt(): SpekTree {
            return SpekTree(
                    "it",
                    ActionType.IT,
                    fakeSpekNodeRunner(),
                    listOf(),
                    false
            )
        }

        fun focusedDescribe(vararg children: SpekTree): SpekTree {
            return SpekTree(
                    "fdescribe",
                    ActionType.DESCRIBE,
                    fakeSpekNodeRunner(),
                    children.toList(),
                    true
            )
        }

        fun unfocusedDescribe(vararg children: SpekTree): SpekTree {
            return SpekTree(
                    "describe",
                    ActionType.DESCRIBE,
                    fakeSpekNodeRunner(),
                    children.toList(),
                    false
            )
        }

        fun fakeSpekNodeRunner(): SpekNodeRunner {
            return object : SpekNodeRunner {
                override fun run(tree: SpekTree, notifier: Notifier, innerAction: () -> Unit) {
                    notifier.start(tree)
                    innerAction()
                }

            }
        }
    }
}