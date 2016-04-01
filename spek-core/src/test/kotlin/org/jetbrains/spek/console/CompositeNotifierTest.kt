package org.jetbrains.spek.console

import org.jetbrains.spek.api.SpekTree
import org.mockito.Mockito.*
import org.junit.Before as before
import org.junit.Test as test

class CompositeNotifierTest {
    val subject = CompositeNotifier()

    val tree = mock(SpekTree::class.java)
    val firstNotifier = mock(ConsoleNotifier::class.java)!!
    val secondNotifier = mock(ConsoleNotifier::class.java)!!
    val throwable = RuntimeException("Test Exception")

    @org.junit.Before fun setup() {
        subject.add(firstNotifier)
        subject.add(secondNotifier)
    }

    @org.junit.Test fun startingATest() {
        subject.start(tree)
        verify(firstNotifier).start(tree)
        verify(secondNotifier).start(tree)
    }

    @org.junit.Test fun passingATest() {
        subject.succeed(tree)
        verify(firstNotifier).succeed(tree)
        verify(secondNotifier).succeed(tree)
    }

    @org.junit.Test fun failingATest() {
        subject.fail(tree, throwable)
        verify(firstNotifier).fail(tree, throwable)
        verify(secondNotifier).fail(tree, throwable)
    }

    @org.junit.Test fun ignoringATest() {
        subject.ignore(tree)
        verify(firstNotifier).ignore(tree)
        verify(secondNotifier).ignore(tree)
    }

    @org.junit.Test fun finishingTheTestSuite() {
        subject.finish()
        verify(firstNotifier).finish()
        verify(secondNotifier).finish()
    }
}
