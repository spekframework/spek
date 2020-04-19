package testData.timeoutTest

import org.spekframework.spek2.Spek

object GlobalTimeoutTest : Spek({
    test("this should run for 10 seconds and but fail since global timeout is 8 seconds") {
        sleep(10000)
    }
})