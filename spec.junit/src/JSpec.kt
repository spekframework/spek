package org.spek.junit.impl

import org.junit.runner.*
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.*
import org.spek.api.*
import org.spek.impl.*
import org.spek.junit.api.*

public class JSpec<T>(val specificationClass: Class<T>) : Runner() {
    public override fun getDescription(): Description? = Description.createSuiteDescription(specificationClass)

    public override fun run(_notifier: RunNotifier?) {
        val notifier = _notifier!!

        Util.safeExecute(null, object : StepListener{
            val root = getDescription()
            override fun executionStarted() {
                notifier.fireTestRunStarted(root)
            }
            override fun executionCompleted() {
                notifier.fireTestRunFinished(Result())
            }
            override fun executionFailed(error: Throwable) {
                notifier.fireTestFailure(Failure(
                        root,
                        error))
            }
        }) {
            if (!javaClass<Spek>().isAssignableFrom(specificationClass)) {
                throw RuntimeException("All spec classes should be inherited from ${javaClass<Spek>()}")
            }

            val spek = (specificationClass.newInstance() as Spek).allGivens()
            spek forEach { given ->

                Runner.executeSpec(given, object : Listener {
                    override fun given(given: TestGivenAction): StepListener {
                        return object : StepListener {
                            override fun executionFailed(error: Throwable) {
                                notifier.fireTestFailure(Failure(
                                        Description.createTestDescription("${given.Description()}", "given"),
                                        error))
                            }
                        }
                    }
                    override fun on(given: TestGivenAction, on: TestOnAction): StepListener {
                        return object : StepListener {
                            override fun executionFailed(error: Throwable) {
                                notifier.fireTestFailure(Failure(
                                        Description.createTestDescription("${given.Description()} : ${on.Description()}", "on"),
                                        error))
                            }
                        }
                    }
                    override fun it(given: TestGivenAction, on: TestOnAction, it: TestItAction): StepListener {
                        val desc = Description.createTestDescription(
                                "${given.Description()} : ${on.Description()}", it.Description())
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
