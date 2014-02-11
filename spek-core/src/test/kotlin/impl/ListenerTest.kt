package org.spek.impl.events

import org.junit.Test as test
import org.mockito.*
import org.spek.impl.*
import kotlin.test.*
import org.junit.*

public class ListenerTest {
    val firstStepListener = Mockito.mock(javaClass<ExecutionReporter>())
    val firstListener = Mockito.mock(javaClass<Listener>())!!

    val secondStepListener = Mockito.mock(javaClass<ExecutionReporter>())
    val secondListener = Mockito.mock(javaClass<Listener>())!!

    val throwable = RuntimeException("Test Exception")

    val multicaster = Multicaster()

    Before fun setup() {
        multicaster.addListener(firstListener)
        multicaster.addListener(secondListener)
    }

    test fun givenExecution() {
        //given two listener with following conditions.
        BDDMockito.given(firstListener.given("Spek", "Test"))!!.willReturn(firstStepListener)
        BDDMockito.given(secondListener.given("Spek", "Test"))!!.willReturn(secondStepListener)
        val stepListener = multicaster.given("Spek", "Test")

        //when execution started.
        stepListener.started();
        //then "executionStarted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.started()
        Mockito.verify(secondStepListener)!!.started()

        //when execution completed.
        stepListener.completed()
        //then "executionCompleted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.completed()
        Mockito.verify(secondStepListener)!!.completed()

        //when execution execution failed.
        stepListener.failed(throwable)
        //then "executionFailed" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.failed(throwable)
        Mockito.verify(secondStepListener)!!.failed(throwable)
    }

    test fun onExecution() {
        //given two listener with following conditions.
        BDDMockito.given(firstListener.on("Spek", "Test", "Test"))!!.willReturn(firstStepListener)
        BDDMockito.given(secondListener.on("Spek", "Test", "Test"))!!.willReturn(secondStepListener)
        val stepListener = multicaster.on("Spek", "Test", "Test")

        //when execution started.
        stepListener.started();
        //then "executionStarted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.started()
        Mockito.verify(secondStepListener)!!.started()

        //when execution completed.
        stepListener.completed()
        //then "executionCompleted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.completed()
        Mockito.verify(secondStepListener)!!.completed()

        //when execution execution failed.
        stepListener.failed(throwable)
        //then "executionFailed" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.failed(throwable)
        Mockito.verify(secondStepListener)!!.failed(throwable)
    }

    test fun itExecution() {
        //given two listener with following conditions.
        BDDMockito.given(firstListener.it("Spek", "Test", "Test", "Test"))!!.willReturn(firstStepListener)
        BDDMockito.given(secondListener.it("Spek", "Test", "Test", "Test"))!!.willReturn(secondStepListener)

        val stepListener = multicaster.it("Spek", "Test", "Test", "Test")

        //when execution started.
        stepListener.started();
        //then "executionStarted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.started()
        Mockito.verify(secondStepListener)!!.started()

        //when execution completed.
        stepListener.completed()
        //then "executionCompleted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.completed()
        Mockito.verify(secondStepListener)!!.completed()

        //when execution execution failed.
        stepListener.failed(throwable)
        //then "executionFailed" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.failed(throwable)
        Mockito.verify(secondStepListener)!!.failed(throwable)
    }
}
