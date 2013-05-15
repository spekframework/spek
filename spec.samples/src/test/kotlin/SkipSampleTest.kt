package org.spek.samples.skip

import org.spek.console.api.ConsoleSpek
import org.spek.impl.Runner
import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.console.listeners.text.PlainTextListener
import org.spek.console.output.console.ConsoleDevice
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import org.spek.api.annotations.skip
import org.spek.console.reflect.SpecificationRunner

class SkipSampleTest {

    test fun test() {
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(ConsoleDevice()))

        SpecificationRunner(listeners).runSpecs("org.spek.samples.skip")
    }
}


class SkipSample: ConsoleSpek() {{
    given("a sample") {
        on("calling a functiono") {
            val result = 10
            it("should return 10") {
                shouldEqual(result, 10)
            }
            skip()
            it("should not return 11") {
                shouldNotEqual(result, 11)
            }
        }
    }
}}

