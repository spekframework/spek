package org.spek.samples.skip

import org.spek.impl.events.Multicaster
import org.junit.Test as test
import org.spek.impl.AbstractSpek
import org.spek.impl.console.reflect.SpecificationRunner
import org.spek.impl.console.listeners.text.PlainTextListener
import org.spek.impl.console.output.console.ConsoleDevice

class SkipSampleTest {

    test fun test() {
        val listeners = Multicaster()
        listeners.addListener(PlainTextListener(ConsoleDevice()))

        SpecificationRunner(listeners).runSpecs("org.spek.samples.skip")
    }
}


class SkipSample: AbstractSpek() {{
    given("a sample") {
        on("calling a function") {
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
}
}

