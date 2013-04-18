package org.spek.impl.events

import org.junit.Test as test
import org.mockito.Mockito
import org.spek.impl.StepListener
import org.mockito.BDDMockito
import kotlin.test.assertEquals
import org.junit.Before

public class ListenerTest {
    val firstStepListener = Mockito.mock(javaClass<StepListener>())
    val firstListener = Mockito.mock(javaClass<Listener>())!!

    val secondStepListener = Mockito.mock(javaClass<StepListener>())
    val secondListener = Mockito.mock(javaClass<Listener>())!!

    val throwable = RuntimeException("Test Exception")

    val multicaster = Multicaster()

    Before fun setup() {
        multicaster.addListener(firstListener)
        multicaster.addListener(secondListener)
    }

    test fun givenExecution() {
        //given two listener with following conditions.
        BDDMockito.given(firstListener.given("Test"))!!.willReturn(firstStepListener)
        BDDMockito.given(secondListener.given("Test"))!!.willReturn(secondStepListener)
        val stepListener = multicaster.given("Test")

        //when execution started.
        stepListener.executionStarted();
        //then "executionStarted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionStarted()
        Mockito.verify(secondStepListener)!!.executionStarted()

        //when execution completed.
        stepListener.executionCompleted()
        //then "executionCompleted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionCompleted()
        Mockito.verify(secondStepListener)!!.executionCompleted()

        //when execution execution failed.
        stepListener.executionFailed(throwable)
        //then "executionFailed" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionFailed(throwable)
        Mockito.verify(secondStepListener)!!.executionFailed(throwable)
    }

    test fun onExecution() {
        //given two listener with following conditions.
        BDDMockito.given(firstListener.on("Test", "Test"))!!.willReturn(firstStepListener)
        BDDMockito.given(secondListener.on("Test", "Test"))!!.willReturn(secondStepListener)
        val stepListener = multicaster.on("Test", "Test")

        //when execution started.
        stepListener.executionStarted();
        //then "executionStarted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionStarted()
        Mockito.verify(secondStepListener)!!.executionStarted()

        //when execution completed.
        stepListener.executionCompleted()
        //then "executionCompleted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionCompleted()
        Mockito.verify(secondStepListener)!!.executionCompleted()

        //when execution execution failed.
        stepListener.executionFailed(throwable)
        //then "executionFailed" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionFailed(throwable)
        Mockito.verify(secondStepListener)!!.executionFailed(throwable)
    }

    test fun itExecution() {
        //given two listener with following conditions.
        BDDMockito.given(firstListener.it("Test", "Test", "Test"))!!.willReturn(firstStepListener)
        BDDMockito.given(secondListener.it("Test", "Test", "Test"))!!.willReturn(secondStepListener)

        val stepListener = multicaster.it("Test", "Test", "Test")

        //when execution started.
        stepListener.executionStarted();
        //then "executionStarted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionStarted()
        Mockito.verify(secondStepListener)!!.executionStarted()

        //when execution completed.
        stepListener.executionCompleted()
        //then "executionCompleted" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionCompleted()
        Mockito.verify(secondStepListener)!!.executionCompleted()

        //when execution execution failed.
        stepListener.executionFailed(throwable)
        //then "executionFailed" in both step listeners must be called.
        Mockito.verify(firstStepListener)!!.executionFailed(throwable)
        Mockito.verify(secondStepListener)!!.executionFailed(throwable)
    }
}
