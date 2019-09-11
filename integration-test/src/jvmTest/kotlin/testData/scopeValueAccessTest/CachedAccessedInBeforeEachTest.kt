package testData.scopeValueAccessTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

class CachedAccessedInBeforeEachTest(mode: CachingMode): Spek({
    val a by memoized(mode) { 1 }

    beforeEachTest { println(a) }

    test("empty test") {}
})