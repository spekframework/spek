package org.spek.impl.events


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

data public class RunStarted(val totalSpecifications: Int)
data public class RunFinished(val passed: Int, val failed: Int, val ignored: Int, val errored: Int)

data public class OnExecuted(val description: String)
data public class OnCompleted(val description: String)
data public class AssertionErrorOccurred(val description: String, val error: AssertionError)

data public class GivenExecuted(val description: String)
data public class GivenCompleted(val description: String)
data public class ItStarted(val description: String)
data public class ItExecuted(val description: String)
data public class ItCompleted(val description: String)
