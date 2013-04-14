package org.spek.junit.impl

import org.junit.runner.*
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.*
import org.spek.api.*
import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.junit.api.*

public class JSpec<T>(val specificationClass: Class<T>) : Runner() {
    private val rootDescription = Description.createSuiteDescription(specificationClass)!!
    public override fun getDescription(): Description? = rootDescription

    public override fun run(_notifier: RunNotifier?) {
        val notifier = _notifier!!
        Util.safeExecute(null, object : StepListener{
            override fun executionStarted() {
                notifier.fireTestStarted(rootDescription)
            }
            override fun executionCompleted() {
                notifier.fireTestFinished(rootDescription)
            }
            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(
                        rootDescription,
                        error))
            }
        }) {
            if (!javaClass<Spek>().isAssignableFrom(specificationClass)) {
                throw RuntimeException("All spec classes should be inherited from ${javaClass<Spek>()}")
            }

            val spek = (specificationClass.newInstance() as JUnitSpek).allGivens()
            spek forEach { given ->

                Runner.executeSpec(given, object : Listener {
                    override fun given(given: String): StepListener {
                        return object : StepListener {
                            override fun executionFailed(error: Throwable) {
                                notifier.fireTestFailure(Failure(
                                        Description.createTestDescription("${given}", "given"),
                                        error))
                            }
                        }
                    }
                    override fun on(given: String, on: String): StepListener {
                        return object : StepListener {
                            override fun executionFailed(error: Throwable) {
                                notifier.fireTestFailure(Failure(
                                        Description.createTestDescription("${given} : ${on}", "on"),
                                        error))
                            }
                        }
                    }
                    override fun it(given: String, on: String, it: String): StepListener {
                        val desc = Description.createTestDescription("${given} : ${on}", it)
                        return object : StepListener {
                            override fun executionStarted() {
                                notifier.fireTestStarted(desc)
                            }
                            override fun executionCompleted() {
                                notifier.fireTestFinished(desc)
                            }
                            override fun executionFailed(error: Throwable) {
                                notifier.fireTestFailure(Failure(desc, error))
                            }
                        }
                    }
                })
            }
        }
    }
}
