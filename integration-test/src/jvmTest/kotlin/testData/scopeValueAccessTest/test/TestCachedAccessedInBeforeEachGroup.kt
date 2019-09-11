package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInBeforeEachGroup: Spek({
    val a by memoized { 1 }

    beforeEachGroup { println(a) }

    test("empty test") {}
})