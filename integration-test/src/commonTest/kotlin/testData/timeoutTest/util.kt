package testData.timeoutTest

expect fun getTimeMillis(): Long

fun sleep(time: Long) {
    val start = getTimeMillis()

    while (true) {
        if (getTimeMillis() - start >= time) {
            break
        }
    }
}
