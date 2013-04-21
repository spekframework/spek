package org.spek.samples

import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.console.listeners.text.PlainTextListener
import org.spek.console.output.console.ConsoleDevice

class SampleIncUtilTest {

    test fun incUtil() {
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(ConsoleDevice()))

        val givenActions = SampleIncUtilSpecs().allGivens()
        givenActions forEach {  Runner.executeSpec(it, listeners) }
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

