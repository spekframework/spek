package org.spek.impl

import org.junit.Test as test
import kotlin.test.assertEquals

public class BackEndTest {

    test fun testGivenImplFunction() {
        //given  a Given with two recorded 'on', first 'on' with a recorded 'it' and the second one with two recorded 'it'
        val spek = SpekImpl()
        spek.given("given Test") {
            this.on("Test On") {
                this.it("Test It") { }
            }
            this.on("Another Test On") {
                this.it("It #1") { }
                this.it("It #2") { }
            }
        }

        val givenAction = spek.allGivens().get(0)
        //when we ask for the description
        //then it should return 'given Test'
        assertEquals("given given Test", givenAction.description())

        //when we ask for 'TestOnAction's,
        //then it should return a list of 2.
        assertEquals(2, givenAction.performInit().size)

        //and the first one must be 'Test On'
        val firstOnAction = givenAction.performInit().get(0)
        assertEquals("Test On", firstOnAction.description())

        //and the second one must be 'Another Test On'
        val secondOnAction = givenAction.performInit().get(1)
        assertEquals("Another Test On", secondOnAction.description())

        //when we ask for 'TestItAction' from the first 'TestOnAction'
        val firstIts = firstOnAction.performInit()
        //then it should return a list of 1
        assertEquals(1, firstIts.size)
        assertEquals("Test It", firstIts.get(0).description())

        //when we ask for 'TestItAction' from the second 'TestOnAction'
        val secondIts = secondOnAction.performInit()
        //then it should return a list of 2
        assertEquals(2, secondIts.size)
        assertEquals("It #1", secondIts.get(0).description())
        assertEquals("It #2", secondIts.get(1).description())
    }

    test fun testGivenImpl() {
        //given a new (empty) 'GivenImpl'
        val given = GivenImpl()
        //when we ask for actions
        //then it should return an empty list
        assertEquals(0, given.getActions().size)

        //when we record a new 'On'
        given.on("Test") { }
        //then it should contains one 'On' action
        assertEquals(1, given.getActions().size)

        //when we record another 'On'
        given.on("Another Test") { }
        //then it should contains two 'On' action
        assertEquals(2, given.getActions().size)
    }

    test fun testOnImpl() {
        //given a new (empty) 'OnImpl'
        val on = OnImpl()
        //when we ask for actions
        //then it should return an empty list
        assertEquals(0, on.getActions().size)

        //when we record a new 'It'
        on.it("Test") { }
        //then it should contains one 'It' action
        assertEquals(1, on.getActions().size)

        //when we record another 'It'
        on.it("Another Test") { }
        //then it should contains two 'It' action
        assertEquals(2, on.getActions().size)
    }
}