package testData.scopeValueAccessTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

class CachedAccessedInAfterEachGroup(mode: CachingMode): Spek({
    val a by memoized(mode) { 1 }

    test("empty test") {}

    afterEachGroup { println(a) }
})