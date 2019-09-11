package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInBeforeGroup: Spek({
    val a by memoized { 1 }

    beforeGroup { println(a) }

    test("empty test") {}
})