package spek.test

import org.junit.Test as test
import spek.*


public class SpecificationRunnerTests {

    test fun creating_a_specification_runner_should_return_instance() {

        val mockListener = MockListener()
        val specRunner = SpecificationRunner(mockListener)

    }


}

public class MockListener(): Listener {

    override fun notify(runStarted: RunStarted) {
        throw UnsupportedOperationException()
    }
    override fun notify(runFinished: RunFinished) {
        throw UnsupportedOperationException()
    }
    override fun notify(onExecuted: OnExecuted) {
        throw UnsupportedOperationException()
    }
    override fun notify(itExecuted: ItExecuted) {
        throw UnsupportedOperationException()
    }
    override fun notify(specExecuted: SpecificationExecuted) {
        throw UnsupportedOperationException()
    }
    override fun notify(assertError: AssertionErrorOccurred) {
        throw UnsupportedOperationException()
    }
    override fun notify(givenExecuted: GivenExecuted) {
        throw UnsupportedOperationException()
    }
    override fun notify(specError: SpecificationErrorOccurred) {
        throw UnsupportedOperationException()
    }

}