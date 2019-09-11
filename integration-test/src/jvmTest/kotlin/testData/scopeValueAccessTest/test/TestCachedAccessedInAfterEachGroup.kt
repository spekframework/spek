package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInAfterEachGroup: Spek({
    val a by memoized { 1 }

    test("empty test") {}

    afterEachGroup { println(a) }
})