package testData.scopeValueAccessTest.scope

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

object ScopeCachedAccessedInBeforeGroup: Spek({
    val a by memoized(CachingMode.SCOPE) { 1 }

    beforeGroup { println(a) }

    test("empty test") {}
})