package testData.scopeValueAccessTest.test

import org.spekframework.spek2.Spek

object TestCachedAccessedInAfterEachTest: Spek({
    val a by memoized { 1 }

    test("empty test") {}

    afterEachTest { println(a) }
})