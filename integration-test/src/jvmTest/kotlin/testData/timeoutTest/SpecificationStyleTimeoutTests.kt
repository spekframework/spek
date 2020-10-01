package testData.timeoutTest

import kotlinx.coroutines.delay
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SpecificationStyleTimeoutTests: Spek({
    describe("timeouts") {
        it("tests running pass 300ms should fail", timeout = 300) {
            delay(400)
        }

        it("tests running less than 500ms should succeed", timeout = 500) {
            delay(200)
        }
    }
})