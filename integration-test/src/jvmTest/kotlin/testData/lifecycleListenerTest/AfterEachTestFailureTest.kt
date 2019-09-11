package testData.lifecycleListenerTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.LifecycleListener
import java.lang.RuntimeException

class AfterEachTestFailureTest(private val listener: LifecycleListener): Spek({
    registerListener(listener)
    test("empty test") {}
    afterEachTest { throw RuntimeException() }
})