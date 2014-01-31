package org.spek.impl

import org.junit.Test as test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.mockito.Matchers

public class UtilTest {
    val listener = Mockito.mock(javaClass<ExecutionReporter>())!!
    val action = Mockito.mock(javaClass<ExecutionReporter>())!!

    test fun successfulSafeExecution() {
        //given an action and a listener
        var verification = false

        //when safeExecution finished successfully.
        executeWithReporting(action, listener) {
            verification = true
        }
        // then the action function has been called.
        assertTrue(verification)
        //and execution started
        Mockito.verify(listener)!!.started()
        // and execution finished.
        Mockito.verify(listener)!!.completed()
        // and execution is not Failed.
        Mockito.verifyNoMoreInteractions(listener)
    }

    test fun failedSafeExecution() {
        //given an action and a listener
       //when safeExecution finished with an error (exception).
        val throwable = RuntimeException("an exception")
        executeWithReporting(action, listener) {
            throw throwable
        }
        //then execution started
        Mockito.verify(listener)!!.started()
        // and execution finished.
        Mockito.verify(listener)!!.completed()
        // but execution Failed.
        Mockito.verify(listener)!!.failed(throwable)
    }
}