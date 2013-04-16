package org.spek.junit.impl

import org.junit.Test as test
import org.spek.api.Spek
import org.mockito.Mockito
import org.junit.runner.notification.RunNotifier
import org.spek.junit.api.JUnitSpek
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.mockito.Matchers
import org.mockito.ArgumentCaptor
import org.junit.runner.Description
import kotlin.test.assertEquals
import org.junit.runner.notification.Failure

public class JSpecTest {
    val notifier = Mockito.mock(javaClass<RunNotifier>())

    test fun successfulRun() {

        //given a spek (MySpek - the below class in this file)
        //when we run this spek
        JSpec(javaClass<MySpek>()).run(notifier)

        //then there must be 5 calls to 'fireTestStarted' in notifier.
        val startCaptor = ArgumentCaptor<Description>()
        Mockito.verify(notifier, Mockito.times(5))!!.fireTestStarted(startCaptor.capture())
        //and descriptions must be as expected
        assertStartDescriptions(startCaptor.getAllValues()!!)

        //and then there must be 5 calls to 'fireTestFinished' in notifier.
        val finishCaptor = ArgumentCaptor<Description>()
        Mockito.verify(notifier, Mockito.times(5))!!.fireTestFinished(finishCaptor.capture())
        //and descriptions must be as expected
        assertFinishDescriptions(finishCaptor.getAllValues()!!)

        //and then there must be a single call to 'fireTestFailure' in notifier.
        val failureCaptor = ArgumentCaptor<Failure>()
        Mockito.verify(notifier)!!.fireTestFailure(failureCaptor.capture())
        // and it should be in the second 'Given', last(only) 'It' operation.
        assertEquals("should fail(given another situation : a failure event)", failureCaptor.getValue()!!.getDescription().toString())
        assertTrue(failureCaptor.getValue()!!.getException() is Throwable)
    }

    private fun assertStartDescriptions(descriptions: List<Description>) {
        //the first one must be the root (or spek class)
        assertEquals("org.spek.junit.impl.MySpek", descriptions.get(0).toString())

        //and the second one must be the first 'It' in the first 'On' in the first 'Given'
        assertEquals("should succeed(given a situation : the successful event)", descriptions.get(1).toString())

        //and the third one must be the second 'It' in the first 'On' in the first 'Given'
        assertEquals("should again succeed(given a situation : the successful event)", descriptions.get(2).toString())

        //and the fourth one must be the first(only) 'It' in the second 'On' in the first 'Given'
        assertEquals("should succeed(given a situation : another successful event)", descriptions.get(3).toString())

        //and the last one must be the first(only) 'It' in the first(only) 'On' in the second 'Given'
        assertEquals("should fail(given another situation : a failure event)", descriptions.get(4).toString())
    }

    private fun assertFinishDescriptions(descriptions: List<Description>) {
        //the first one must be the first 'It' in the first 'On' in the first 'Given'
        assertEquals("should succeed(given a situation : the successful event)", descriptions.get(0).toString())

        //and the second one must be the second 'It' in the first 'On' in the first 'Given'
        assertEquals("should again succeed(given a situation : the successful event)", descriptions.get(1).toString())

        //and the third one must be the first(only) 'It' in the second 'On' in the first 'Given'
        assertEquals("should succeed(given a situation : another successful event)", descriptions.get(2).toString())

        //and the fourth one must be the first(only) 'It' in the first(only) 'On' in the second 'Given'
        assertEquals("should fail(given another situation : a failure event)", descriptions.get(3).toString())

        //and the last one must be the root (or spek class)
        assertEquals("org.spek.junit.impl.MySpek", descriptions.get(4).toString())
    }
}

class MySpek : JUnitSpek() {{
    given("a situation") {
        on("the successful event") {
            it("should succeed") {
                assertTrue(true)
            }
            it("should again succeed") {
                assertFalse(false)
            }
        }

        on("another successful event") {
            it("should succeed") {
                assertTrue(true)
            }
        }
    }

    given("another situation") {
        on("a failure event") {
            it("should fail") {
                assertTrue(false)
            }
        }
    }
}}
