package org.spek.console.api

import org.junit.Test as test
import kotlin.test.assertEquals

public class APITest {

    test fun allGivens() {
        //given a spek
        val spek = object: ConsoleSpek() {}
        //when we add 3 given
        spek.given("First") {}
        spek.given("Second") {}
        spek.given("Third") {}
        //then there must be 3 TestGivenActions
        assertEquals(3, spek.allGiven().size)
        assertEquals("given First", spek.allGiven().get(0).description())
        assertEquals("given Second", spek.allGiven().get(1).description())
        assertEquals("given Third", spek.allGiven().get(2).description())
    }
}
