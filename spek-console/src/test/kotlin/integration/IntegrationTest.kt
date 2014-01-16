package org.spek.console.integration

import org.junit.Test as test
import org.spek.console.reflect.SpecificationRunner
import org.spek.console.listeners.text.PlainTextListener
import org.spek.console.output.console.ConsoleDevice

public class ConsoleIntegrationTest {

    test public fun testSampleSpec1() {
        SpecificationRunner(PlainTextListener(ConsoleDevice())).runSpecs("org.spek.console.test.samples")
    }

    test public fun testSampleSpec2() {
        SpecificationRunner(PlainTextListener(ConsoleDevice())).runSpecs("org.spek.console.test.samples2")
    }
}
