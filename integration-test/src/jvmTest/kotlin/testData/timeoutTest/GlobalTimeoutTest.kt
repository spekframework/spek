package testData.timeoutTest

import org.spekframework.spek2.Spek

object GlobalTimeoutTest : Spek({
    test("this should run for 15 seconds and pass since global timeout is 20") {
        sleep(15000)
    }
})