package spek


public trait Listener {
    fun notify(runStarted: RunStarted)
    fun notify(runFinished: RunFinished)
    fun notify(onExecuted: OnExecuted)
    fun notify(itExecuted: ItExecuted)
    fun notify(specExecuted: SpecificationExecuted)
    fun notify(assertError: AssertionErrorOccurred)
    fun notify(givenExecuted: GivenExecuted)
    fun notify(specError: SpecificationErrorOccurred)
}

