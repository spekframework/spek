package org.jetbrains.spek.junit

import org.jetbrains.spek.api.PendingException
import org.jetbrains.spek.api.SkippedException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.SpekTestableComponent
import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.junit.runners.ParentRunner
import java.io.Serializable
import java.util.*

data class JUnitUniqueId(val id: Int) : Serializable {
    companion object {
        var id = 0
        fun next() = JUnitUniqueId(id++)
    }
}

data class SpekResult(val successful: Boolean = false,
                      val exception: Throwable? = null)

public fun runSpek(testIdHashCode: Int, results: HashMap<Int, SpekResult>, action: () -> Unit) {
    try {
        action()
        results.put(testIdHashCode, SpekResult(successful = true))
    } catch(e: Throwable) {
        results.put(testIdHashCode, SpekResult(exception = e))
    }
}

public fun evaluateResults(desc: Description?, notifier: RunNotifier?, results: HashMap<Int, SpekResult>) {
    desc?.apply {
        val testId = hashCode()
        val result = results[testId]
        notifier?.apply {
            // if (isTest)
            fireTestStarted(desc)
            children?.forEach { child -> evaluateResults(child, notifier, results) }
            when (result?.exception) {
                is SkippedException -> fireTestAssumptionFailed(Failure(desc, result?.exception))
                is PendingException -> fireTestIgnored(desc)
                is Throwable -> fireTestFailure(Failure(desc, result?.exception))
            }
            // if (isTest)
            fireTestFinished(desc)
        }
    }
}

public class JUnitClassRunner<T>(val specClass: Class<T>,
                                 val specInstance: T? = null) : ParentRunner<Unit>(specClass) {

    constructor(specClass: Class<T>) : this(specClass, null) {
    }

    val _spekRunResults: HashMap<Int, SpekResult> = HashMap()

    val _description by lazy(LazyThreadSafetyMode.NONE) {
        val specInstance = (specInstance as? Spek) ?: (specClass.newInstance() as? Spek)
        val suiteDesc = when (specInstance) {
            is SpekTestableComponent -> Description.createSuiteDescription(specInstance.description(), JUnitUniqueId.next())
            else -> Description.createSuiteDescription(specClass)
        }
        runSpek(suiteDesc.hashCode(), _spekRunResults) {
            specInstance?.listGiven()?.forEach { givenSpek ->
                val givenId = JUnitUniqueId.next()
                val givenDesc = Description.createSuiteDescription(givenSpek.description(), givenId)
                suiteDesc.addChild(givenDesc)
                runSpek(givenId.hashCode(), _spekRunResults) {
                    givenSpek.listOn().forEach { onSpek ->
                        val onId = JUnitUniqueId.next()
                        val onDesc = Description.createSuiteDescription(onSpek.description(), onId)
                        givenDesc.addChild(onDesc)
                        runSpek(onId.hashCode(), _spekRunResults) {
                            givenSpek.run {
                                onSpek.run {
                                    onSpek.listIt().forEach { itSpek ->
                                        val itId = JUnitUniqueId.next()
                                        val itDesc = Description.createSuiteDescription(itSpek.description(), itId)
                                        onDesc.addChild(itDesc)
                                        runSpek(itId.hashCode(), _spekRunResults) { itSpek.run {} }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        suiteDesc
    }

    override fun getDescription(): Description = _description

    override fun run(notifier: RunNotifier?) = evaluateResults(_description, notifier, _spekRunResults)

    override fun getChildren(): MutableList<Unit> = ArrayList()

    protected override fun describeChild(child: Unit?): Description? = null

    protected override fun runChild(child: Unit?, notifier: RunNotifier?) {
    }
}
