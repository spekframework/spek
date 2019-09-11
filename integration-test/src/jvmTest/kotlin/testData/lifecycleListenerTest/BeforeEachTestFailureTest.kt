package testData.lifecycleListenerTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.LifecycleListener
import java.lang.RuntimeException

class BeforeEachTestFailureTest(private val listener: LifecycleListener): Spek({
    registerListener(listener)
    beforeEachTest { throw RuntimeException() }
    test("empty test") {}
})