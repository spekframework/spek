package org.spek.samples

import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.console.listeners.text.PlainTextListener
import kotlin.test.assertEquals

class SampleIncUtilTest {

    test fun incUtil() {
        val buffer = StringBuilder()
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(BufferedOutputDevice(buffer)))

        val givenActions = SampleIncUtilSpecs().allGivens()
        givenActions forEach {  Runner.executeSpec(it, listeners) }

        assertEquals(expected(), buffer.toString())
    }

    private fun expected(): String {
        return "Given given an inc util" +
        "On calling incVaueBy with 4 and given number 6" +
        "It should return 10"
    }
}


class SampleIncUtilSpecs: ConsoleSpek() {{
    given("an inc util") {
        val incUtil = SampleIncUtil()
        on("calling incVaueBy with 4 and given number 6") {
            val result = incUtil.incValueBy(4, 6)
            it("should return 10") {
                shouldEqual(result, 10)
            }
        }
    }
}}

class SampleIncUtil {
    fun incValueBy(value: Int, inc: Int) = value + inc
}

