package testData.timeoutTest

import kotlinx.coroutines.channels.ReceiveChannel
import org.spekframework.spek2.Spek

class GlobalTimeoutTest(latch: ReceiveChannel<Int>): Spek({
    test("this should run for 10s and pass since global timeout is 15s") {
        latch.receive()
    }
})