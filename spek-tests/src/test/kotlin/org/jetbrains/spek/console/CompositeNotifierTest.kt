package org.jetbrains.spek.console

import org.jetbrains.spek.api.TestAction
import org.mockito.Mockito
import org.junit.Before as before
import org.junit.Test as test

class CompositeNotifierTest {
    val firstNotifier = Mockito.mock(ConsoleNotifier::class.java)!!
    val secondNotifier = Mockito.mock(ConsoleNotifier::class.java)!!
    val throwable = RuntimeException("Test Exception")
    val multicaster = CompositeNotifier()
    val testAction = Mockito.mock(TestAction::class.java)

    @org.junit.Before fun setup() {
        multicaster.add(firstNotifier)
        multicaster.add(secondNotifier)
    }

    @org.junit.Test fun startingATest() {
        multicaster.start(testAction)
        Mockito.verify(firstNotifier).start(testAction)
        Mockito.verify(secondNotifier).start(testAction)
    }

    @org.junit.Test fun passingATest() {
        multicaster.succeed(testAction)
        Mockito.verify(firstNotifier).succeed(testAction)
        Mockito.verify(secondNotifier).succeed(testAction)
    }

    @org.junit.Test fun failingATest() {
        multicaster.fail(testAction, throwable)
        Mockito.verify(firstNotifier).fail(testAction, throwable)
        Mockito.verify(secondNotifier).fail(testAction, throwable)
    }

    @org.junit.Test fun ignoringATest() {
        multicaster.ignore(testAction)
        Mockito.verify(firstNotifier).ignore(testAction)
        Mockito.verify(secondNotifier).ignore(testAction)
    }

    @org.junit.Test fun finishingTheTestSuite() {
        multicaster.finish()
        Mockito.verify(firstNotifier).finish()
        Mockito.verify(secondNotifier).finish()
    }
}
