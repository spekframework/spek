package testData.timeoutTest

fun getTimeMillis(): Long = System.currentTimeMillis()

fun sleep(time: Long) {
    val start = getTimeMillis()

    while (true) {
        if (getTimeMillis() - start >= time) {
            break
        }
    }
}
