package testData.timeoutTest

import java.util.concurrent.CountDownLatch

fun sleep(time: Long) {
    val latch = CountDownLatch(1)
    val thread = Thread {
        Thread.sleep(time)
        latch.countDown()
    }
    thread.start()
    latch.await()
}
