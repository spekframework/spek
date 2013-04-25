package org.spek.samples

import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.console.listeners.text.PlainTextListener
import org.spek.console.output.console.ConsoleDevice
import kotlin.test.assertEquals

class SampleIncUtilTest {

    test fun incUtil() {
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(ConsoleDevice()))

        val givenActions = IncUtilConsoleSpecs().allGivens()
        givenActions forEach {  Runner.executeSpec(it, listeners) }
    }
}


class IncUtilConsoleSpecs: ConsoleSpek() {{
    given("an inc util") {

        val incUtil = SampleIncUtil()

        on("calling incVaueBy with 4 and given number 6") {

            val result = incUtil.incValueBy(4, 6)

            it("should return 10") {

                shouldEqual(result, 10)

            }
        }

        on("calling incValueBy with 10 and given number 2") {
            val result = incUtil.incValueBy(10, 2)

            it("shut return 12") {
                assertEquals(result, 10)
            }
        }
    }
}}


