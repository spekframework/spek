package testData.scopeValueAccessTest.scope

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

object ScopeCachedAccessedInAfterGroup: Spek({
    val a by memoized(CachingMode.SCOPE) { 1 }

    test("empty test") {}

    afterGroup { println(a) }
})