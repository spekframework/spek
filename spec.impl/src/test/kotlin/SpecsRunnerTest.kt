package org.spek.impl

import org.junit.Test as test
import org.spek.impl.events.Listener
import org.mockito.Mockito
import org.mockito.BDDMockito
import org.mockito.Matchers

public class SpecsRunnerTest {
    val listener = Mockito.mock(javaClass<Listener>())!!
    val stepListener = Mockito.mock(javaClass<StepListener>())
    val testGivenAction = Mockito.mock(javaClass<TestGivenAction>())!!
    val aTestOnAction = Mockito.mock(javaClass<TestOnAction>())!!
    val anotherTestOnAction = Mockito.mock(javaClass<TestOnAction>())!!
    val aTestItAction = Mockito.mock(javaClass<TestItAction>())!!
    val anotherTestItAction = Mockito.mock(javaClass<TestItAction>())!!

    test fun executeSpec() {
        BDDMockito.given(listener.given(Matchers.anyString()!!))!!
                .willReturn(stepListener)
        BDDMockito.given(listener.on(Matchers.anyString()!!, Matchers.anyString()!!))!!
                .willReturn(stepListener)
        BDDMockito.given(listener.it(Matchers.anyString()!!, Matchers.anyString()!!, Matchers.anyString()!!))!!
                .willReturn(stepListener)

        //given a GivenAction with 2 OnActions
        BDDMockito.given(testGivenAction.performInit())!!.willReturn(listOf(aTestOnAction, anotherTestOnAction))
        //where the first OnAction has 2 ItActions (same instance)
        BDDMockito.given(aTestOnAction.performInit())!!.willReturn(listOf(aTestItAction, aTestItAction))
        //and the second OnAction has 2 ItActions (different instances)
        BDDMockito.given(anotherTestOnAction.performInit())!!.willReturn(listOf(aTestItAction, anotherTestItAction))

        //when we call executeSpec
        Runner.executeSpec(testGivenAction, listener)

        // then there must be 3 calls to aTestItAction.run(). "2 call for first ItAction and 1 call for second ItAction)
        Mockito.verify(aTestItAction, Mockito.times(3))!!.run()
        // and there must only 1 call to anotherTestItAction.run(). "only used once in ItAction)
        Mockito.verify(anotherTestItAction)!!.run()
    }
}