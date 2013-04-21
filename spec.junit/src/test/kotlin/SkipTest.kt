package org.spek.junit.impl

import org.junit.Test as test
import org.spek.junit.api.JUnitSpek
import org.junit.runner.notification.RunNotifier
import org.mockito.Mockito
import org.mockito.ArgumentCaptor
import org.junit.runner.Description

public class SkipTest {
    val notifier = Mockito.mock(javaClass<RunNotifier>())

    test fun skipIt() {

        JSpec(javaClass<SkipItSpek>()).run(notifier)

        val startCaptor = ArgumentCaptor<Description>()
        Mockito.verify(notifier, Mockito.times(1))!!.fireTestIgnored(startCaptor.capture())
        println(startCaptor.getValue())
    }
}

class SkipItSpek : JUnitSpek() {{
    given("as a programmer") {
        on("a basic math operation for me") {
            skip("not ready")
                    .it("should be relatively easy") {}
            it(" should be? really?!") {}
        }
    }
}}
