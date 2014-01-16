package org.spek.junit.test.samples2

import org.spek.api.annotations.spek
import org.spek.api.Spek
import org.spek.junit.api.SpekJUnitPackageRunner
import org.junit.runner.RunWith

spek fun Spek.junitSampleSpec() {
    given("it's a good weather"){
        on("a traffic jam") {
            it("should not be fun") {
                shouldBeTrue(true)
            }
        }
    }
    given("something") {
        on("event") {
            it("should be not fun") {
                shouldBeFalse(true)
            }
        }
    }
}

//some magic to include package into JUnit test
RunWith(javaClass<RunInJUnit>())
public class RunInJUnit(val _ : Class<*>) : SpekJUnitPackageRunner(_)
