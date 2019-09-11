package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInAfterGroup: Spek({
    val a by memoized { 1 }

    test("empty test") {}

    afterGroup { println(a) }
})