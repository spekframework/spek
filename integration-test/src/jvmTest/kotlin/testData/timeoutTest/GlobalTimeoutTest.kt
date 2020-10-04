package testData.timeoutTest

import kotlinx.coroutines.delay
import org.spekframework.spek2.Spek

object GlobalTimeoutTest: Spek({
    test("this should run for 300ms and fail since global timeout is 100ms") {
        delay(300)
    }
})