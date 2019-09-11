package testData.lifecycleListenerTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.LifecycleListener

class TestFailureTest(private val listener: LifecycleListener): Spek({
    registerListener(listener)
    test("this should fail") {
        throw RuntimeException()
    }
})