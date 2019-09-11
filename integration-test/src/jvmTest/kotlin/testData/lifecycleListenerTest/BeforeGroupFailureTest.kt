package testData.lifecycleListenerTest

import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.LifecycleListener
import java.lang.RuntimeException

class BeforeGroupFailureTest(private val listener: LifecycleListener): Spek({
    registerListener(listener)
    beforeGroup { throw RuntimeException() }
    test("empty test") {}
})