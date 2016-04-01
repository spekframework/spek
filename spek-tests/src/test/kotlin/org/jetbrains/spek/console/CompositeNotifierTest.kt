package org.jetbrains.spek.console

import org.jetbrains.spek.api.SpekTree
import org.mockito.Mockito
import org.junit.Before as before
import org.junit.Test as test

class CompositeNotifierTest {
    val subject = CompositeNotifier()

    val tree = Mockito.mock(SpekTree::class.java)
    val firstNotifier = Mockito.mock(ConsoleNotifier::class.java)!!
    val secondNotifier = Mockito.mock(ConsoleNotifier::class.java)!!
    val throwable = RuntimeException("Test Exception")

    @org.junit.Before fun setup() {
        subject.add(firstNotifier)
        subject.add(secondNotifier)
    }

    @org.junit.Test fun startingATest() {
        subject.start(tree)
        Mockito.verify(firstNotifier).start(tree)
        Mockito.verify(secondNotifier).start(tree)
    }

    @org.junit.Test fun passingATest() {
        subject.succeed(tree)
        Mockito.verify(firstNotifier).succeed(tree)
        Mockito.verify(secondNotifier).succeed(tree)
    }

    @org.junit.Test fun failingATest() {
        subject.fail(tree, throwable)
        Mockito.verify(firstNotifier).fail(tree, throwable)
        Mockito.verify(secondNotifier).fail(tree, throwable)
    }

    @org.junit.Test fun ignoringATest() {
        subject.ignore(tree)
        Mockito.verify(firstNotifier).ignore(tree)
        Mockito.verify(secondNotifier).ignore(tree)
    }

    @org.junit.Test fun finishingTheTestSuite() {
        subject.finish()
        Mockito.verify(firstNotifier).finish()
        Mockito.verify(secondNotifier).finish()
    }
}
