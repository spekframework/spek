package org.spek.junit.api

import org.junit.Test as test
import kotlin.test.assertEquals

public class APITest {

    test fun allGivens() {
        //given a spek
        val spek = object: JUnitSpek() {}
        //when we add 3 given
        spek.given("First") {}
        spek.given("Second") {}
        spek.given("Third") {}
        //then there must be 3 TestGivenActions
        assertEquals(3, spek.allGivens().size)
        assertEquals("given First", spek.allGivens().get(0).description())
        assertEquals("given Second", spek.allGivens().get(1).description())
        assertEquals("given Third", spek.allGivens().get(2).description())
    }
}
