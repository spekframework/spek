package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInBeforeEachTest: Spek({
    val a by memoized { 1 }

    beforeEachTest { println(a) }

    test("empty test") {}
})