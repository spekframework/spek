package testData.scopeValueAccessTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

class CachedAccessedInBeforeGroup(mode: CachingMode): Spek({
    val a by memoized(mode) { 1 }

    beforeGroup { println(a) }

    test("empty test") {}
})