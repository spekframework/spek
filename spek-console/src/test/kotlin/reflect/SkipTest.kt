package org.spek.console.impl

import org.junit.Test as test
import org.junit.runner.notification.RunNotifier
import org.mockito.Mockito
import org.spek.impl.events.Multicaster
import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.console.reflect.BufferedOutputDevice
import org.spek.console.listeners.text.PlainTextListener
import kotlin.test.assertEquals
import org.spek.api.annotations.skip
import org.spek.reflect.SpecificationRunner

public class SkipTest {
    val notifier = Mockito.mock(javaClass<RunNotifier>())
    val listeners = Multicaster()
    val buffer = StringBuilder()

    test fun skipIt() {

        val expectedIt = """
Given given a situation
  On an event
    It should A
    Skipped It 'should A'. Reason: not ready yet
    It should B
"""
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SkipItSpek().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expectedIt.trim(), buffer.toString().trim())
    }

    test fun skipOn() {

        val expectedOn = """
Given given a situation
  On an event
  Skipped On 'an event'. Reason: not ready yet
  On another event
    It should B
"""
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SkipOnSpek().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expectedOn.trim(), buffer.toString().trim())
    }


    test fun skipGiven() {

        val expectedGiven = """
Given given a situation
Skipped Given 'given a situation'. Reason: for some reason
"""
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SkipGivenSpek().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expectedGiven.trim(), buffer.toString().trim())
    }

    test fun skipSpek() {
        //TODO
    }

    test fun pendingIt() {

        val expectedIt = """
Given given a situation
  On an event
    It should A
    Pending It 'should A'. Reason: not implemented yet
    It should B
    Pending It 'should B'. Reason: not given
"""
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = PendingItSpek().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expectedIt.trim(), buffer.toString().trim())
    }

    test fun pendingOn() {

        val expectedOn = """
Given given a situation
  On an event
  Pending On 'an event'. Reason: not given
  On another event
  Pending On 'another event'. Reason: not implemented yet
"""
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = PendingOnSpek().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expectedOn.trim(), buffer.toString().trim())
    }


    test fun pendingGiven() {

        val expectedGiven = """
Given given a situation
Pending Given 'given a situation'. Reason: for some reason

Given given another situation
Pending Given 'given another situation'. Reason: not given
"""
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = PendingGivenSpek().allGivens()
        givenActions forEach { Runner.executeSpec(it, listeners) }

        assertEquals(expectedGiven.trim(), buffer.toString().trim())
    }
}

class SkipItSpek: ConsoleSpek() {{
    given("a situation") {
        on("an event") {
            it("should A") {
                skip("not ready yet")
            }

            it("should B") { }
        }
    }
}
}

class SkipOnSpek: ConsoleSpek() {{
    given("a situation") {
        on("an event") {
            skip("not ready yet")

            it("should A") { }
        }

        on("another event") {

            it("should B") { }
        }
    }
}
}

class SkipGivenSpek: ConsoleSpek() {{
    given("a situation") {
        skip("for some reason")

        on("an event") {
            it("should A") { }
        }

        on("another event") {
            it("should B") {
                pending("ok!!!")
            }
        }
    }
}
}

skip("for some reason") class SkipSpek: ConsoleSpek() {{
}
}

class PendingItSpek: ConsoleSpek() {{
    given("a situation") {
        on("an event") {
            it("should A") {
                pending("not implemented yet")
            }

            //default pending.
            it("should B")
        }
    }
}
}

class PendingOnSpek: ConsoleSpek() {{
    given("a situation") {
        //default pending.
        on("an event")

        on("another event") {
            pending("not implemented yet")
        }
    }
}
}

class PendingGivenSpek: ConsoleSpek() {{
    given("a situation") {
        pending("for some reason")
    }

    //default pending.
    given("another situation")
}
}
