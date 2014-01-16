package org.spek.impl

import org.junit.Test as test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.mockito.Matchers

public class UtilTest {
    val listener = Mockito.mock(javaClass<StepListener>())!!
    val action = Mockito.mock(javaClass<StepListener>())!!

    test fun successfulSafeExecution() {
        //given an action and a listener
        var verification = false

        //when safeExecution finished successfully.
        Util.safeExecute(action, listener) {
            verification = true
        }
        // then the action function has been called.
        assertTrue(verification)
        //and execution started
        Mockito.verify(listener)!!.executionStarted()
        // and execution finished.
        Mockito.verify(listener)!!.executionCompleted()
        // and execution is not Failed.
        Mockito.verifyNoMoreInteractions(listener)
    }

    test fun failedSafeExecution() {
        //given an action and a listener
       //when safeExecution finished with an error (exception).
        val throwable = RuntimeException("an exception")
        Util.safeExecute(action, listener) {
            throw throwable
        }
        //then execution started
        Mockito.verify(listener)!!.executionStarted()
        // and execution finished.
        Mockito.verify(listener)!!.executionCompleted()
        // but execution Failed.
        Mockito.verify(listener)!!.executionFailed(throwable)
    }
}