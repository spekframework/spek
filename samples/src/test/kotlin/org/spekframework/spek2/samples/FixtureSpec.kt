package org.spekframework.spek2.samples

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek

/**
 * @author Ranie Jade Ramiso
 */
class FixtureSpec : Spek({
    var counter = 0
    beforeEachTest {
        counter++
    }

    describe("a number") {
        beforeEachTest {
            counter++
        }

        it("should be 2") {
            assertThat(counter, equalTo(2))
        }

        it("should be 2 as well") {
            assertThat(counter, equalTo(2))
        }

        afterEachTest {
            counter--
        }
    }

    afterEachTest {
        counter--
    }
})
