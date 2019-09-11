package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInTest: Spek({
    val a by memoized { 1 }

    test("empty test") {
        println(a)
    }
})