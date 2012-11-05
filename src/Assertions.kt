package spek

import kotlin.test.assertEquals

fun shouldEqual<T>(expected: T, actual: T) {
    assertEquals(expected, actual)
}
