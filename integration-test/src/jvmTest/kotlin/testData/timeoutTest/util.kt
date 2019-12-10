package testData.timeoutTest

import kotlinx.coroutines.delay

suspend fun sleep(time: Long) {
    delay(time)
}
