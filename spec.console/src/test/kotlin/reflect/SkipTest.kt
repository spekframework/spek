package org.spek.console.impl

import org.junit.Test as test
import org.junit.runner.notification.RunNotifier
import org.mockito.Mockito
import org.mockito.ArgumentCaptor
import org.junit.runner.Description
import org.spek.impl.events.Multicaster
import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.console.reflect.BufferedOutputDevice
import org.spek.console.listeners.text.PlainTextListener
import kotlin.test.assertEquals

public class SkipTest {
    val notifier = Mockito.mock(javaClass<RunNotifier>())
    val listeners = Multicaster()
    val buffer = StringBuilder()

    test fun skipIt() {
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SkipItSpek().allGivens()
        givenActions forEach {  Runner.executeSpec(it, listeners) }

        assertEquals(expectedIt(), buffer.toString())
    }


    test fun skipOn() {
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SkipOnSpek().allGivens()
        givenActions forEach {  Runner.executeSpec(it, listeners) }

        assertEquals(expectedOn(), buffer.toString())
    }

    private fun expectedIt(): String {
        return "Given given a situation" +
        "On an event" +
        "Skipped: It should A - Reason: not ready yet" +
        "It should B"
    }

    private fun expectedOn(): String {
        return "Given given a situation" +
        "Skipped: On an event - Reason: not ready yet" +
        "On another event" +
        "It should B"
    }
}

class SkipItSpek : ConsoleSpek() {{
    given("a situation") {
        on("an event") {

            skip("not ready yet")
            it("should A") {}

            it("should B") {}
        }
    }
}}

class SkipOnSpek : ConsoleSpek() {{
    given("a situation") {
        skip("not ready yet")
        on("an event") {

            it("should A") {}
        }

        on("another event") {

            it("should B") {}
         }
    }
}}
