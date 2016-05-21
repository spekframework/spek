package org.jetbrains.spek.console

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SpekTree
import org.mockito.Mockito.*


class CompositeNotifierTest : Spek({
    var subject : CompositeNotifier = CompositeNotifier()

    val tree = mock(SpekTree::class.java)
    val firstNotifier = mock(ConsoleNotifier::class.java)!!
    val secondNotifier = mock(ConsoleNotifier::class.java)!!
    val throwable = RuntimeException("Test Exception")

    beforeEach {
        subject = CompositeNotifier()
        subject.add(firstNotifier)
        subject.add(secondNotifier)
    }

    afterEach {
        reset(tree, firstNotifier, secondNotifier)
    }

    it("delegates start to added notifiers") {
        subject.start(tree)
        verify(firstNotifier).start(tree)
        verify(secondNotifier).start(tree)
    }

    it("delegates succeed to added notifiers") {
        subject.succeed(tree)
        verify(firstNotifier).succeed(tree)
        verify(secondNotifier).succeed(tree)
    }

    it("delegates fail to added notifiers") {
        subject.fail(tree, throwable)
        verify(firstNotifier).fail(tree, throwable)
        verify(secondNotifier).fail(tree, throwable)
    }

    it("delegates ignore to added notifiers") {
        subject.ignore(tree)
        verify(firstNotifier).ignore(tree)
        verify(secondNotifier).ignore(tree)
    }

    it("delegates finish to added notifiers") {
        subject.finish()
        verify(firstNotifier).finish()
        verify(secondNotifier).finish()
    }
})
