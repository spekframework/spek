package org.jetbrains.spek.api

import org.junit.Test as test
import org.mockito.*
import kotlin.test.*
import org.jetbrains.spek.console.ActionStatusReporter
import org.jetbrains.spek.console.executeWithReporting

class UtilTest {
    val listener = Mockito.mock(ActionStatusReporter::class.java)!!
    val action = Mockito.mock(ActionStatusReporter::class.java)!!

    @test fun successfulSafeExecution() {
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

    @test fun failedSafeExecution() {
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