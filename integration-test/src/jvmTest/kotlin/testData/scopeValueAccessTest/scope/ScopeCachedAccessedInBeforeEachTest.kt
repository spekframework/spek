package testData.scopeValueAccessTest.scope

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode

object ScopeCachedAccessedInBeforeEachTest: Spek({
    val a by memoized(CachingMode.SCOPE) { 1 }

    beforeEachTest { println(a) }

    test("empty test") {}
})