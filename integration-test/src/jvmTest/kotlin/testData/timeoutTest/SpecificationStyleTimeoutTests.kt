package testData.timeoutTest

import kotlinx.coroutines.channels.ReceiveChannel
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SpecificationStyleTimeoutTests(latch: ReceiveChannel<Int>): Spek({
    describe("timeouts") {
        it("tests running pass 300ms should fail", timeout = 300) {
            latch.receive()
        }

        it("tests running less than default timeout should succeed") {
            latch.receive()
        }

        it("tests running less than 500ms should succeed", timeout = 500) {
            latch.receive()
        }
    }
})