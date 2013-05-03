package org.spek.impl.junit

import org.junit.runner.*
import org.junit.runner.notification.*
import org.spek.api.*
import org.spek.impl.*
import org.spek.impl.events.*

public class JSpec<T>(val specificationClass: Class<T>): Runner() {
    private val rootDescription = Description.createSuiteDescription(specificationClass)!!
    public override fun getDescription(): Description? = rootDescription

    public override fun run(_notifier: RunNotifier?) {
        val notifier = _notifier!!
        Util.safeExecute(null, object : StepListener {
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

            /*
            * TODO: need to be refactored when #KT-3534 got fixed.
            */
            var skipped = false
            val annotations = specificationClass.getAnnotations()!!
            for (annotation in annotations) {
                val clazz = annotation!!.annotationType()
                if (clazz == javaClass<skip>()) {
                    skipped = true
                    notifier.fireTestIgnored(rootDescription)
                }
            }
            if (!skipped) {
                val givens = (specificationClass.newInstance() as AbstractSpek).allGivens()
                givens forEach { given ->

                    Runner.executeSpec(given, object : Listener {

                        override fun spek(spek: String): StepListener {
                            return object : StepListener {
                                override fun executionSkipped(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$spek", "Skipped. Reason: $why"))
                                }
                            }
                        }

                        override fun given(given: String): StepListener {
                            return object : StepListener {
                                override fun executionSkipped(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$given", "Skipped. Reason: $why"))
                                }
                                override fun executionPending(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$given", "Pending. Reason: $why"))
                                }
                                override fun executionFailed(error: Throwable) {
                                    notifier.fireTestFailure(Failure(Description
                                            .createTestDescription("$given", "given"), error))
                                }
                            }
                        }

                        override fun on(given: String, on: String): StepListener {

                            return object : StepListener {

                                override fun executionSkipped(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$given : $on", "Skipped. Reason: $why"))

                                }
                                override fun executionPending(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$given : $on", "Pending. Reason: $why"))

                                }
                                override fun executionFailed(error: Throwable) {
                                    notifier.fireTestFailure(Failure(
                                            Description.createTestDescription("$given : $on", "on"),
                                            error))
                                }
                            }
                        }

                        override fun it(given: String, on: String, it: String): StepListener {

                            val desc = Description.createTestDescription("$given : $on", it)

                            return object : StepListener {
                                override fun executionStarted() {
                                    notifier.fireTestStarted(desc)
                                }

                                override fun executionCompleted() {
                                    notifier.fireTestFinished(desc)
                                }

                                override fun executionSkipped(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$given : $on : $it", "Skipped. Reason: $why"))
                                }

                                override fun executionPending(why: String) {
                                    notifier.fireTestIgnored(Description
                                            .createTestDescription("$given : $on : $it", "Pending. Reason: $why"))
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
}
