package org.spek.junit

import org.junit.runner.RunWith
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.spek.Reflection
import org.spek.SpecificationFinder
import org.spek.Listener
import org.spek.RunStarted
import org.spek.RunFinished
import org.spek.OnExecuted
import org.spek.ItExecuted
import org.spek.SpecificationExecuted
import org.spek.AssertionErrorOccurred
import org.spek.GivenExecuted
import org.spek.SpecificationErrorOccurred
import org.junit.runner.notification.Failure
import org.junit.runner.Result
import org.spek.Spek
import org.spek.SpecificationRunner
import org.spek.EventHandler
import org.spek.OnCompleted
import org.spek.ItStarted
import org.spek.ItCompleted

public class JSpec<T>(val clazz : Class<T>) : Runner() {
    public override fun getDescription(): Description? = Description.createSuiteDescription("Spec:" + clazz.getName())

    private fun println(s : Any?) {}

    public override fun run(_notifier: RunNotifier?) {
        val notifier = _notifier!!

        notifier.fireTestRunStarted(getDescription())

        if (!javaClass<Spek>().isAssignableFrom(clazz)) {
            throw RuntimeException("All spec classes should be inherited from ${javaClass<Spek>()}")
        }

        val spek = clazz.newInstance() as Spek
        var onName : String? = null
        org.spek.eventAggregator.subscribe("OnExecuted", object:EventHandler {
            override fun handle(data: Any) {
                val d = data as OnExecuted
                onName = d.description
//                notifier.fireTestStarted(Description.createSuiteDescription(d.description))
                println(data)
            }
        })
        org.spek.eventAggregator.subscribe("OnCompleted", object:EventHandler {
            override fun handle(data: Any) {
                val d = data as OnCompleted
//                notifier.fireTestFinished(Description.createSuiteDescription(d.description))
                println(data)
                onName = null
            }
        })

        org.spek.eventAggregator.subscribe("ItStarted", object:EventHandler {
            override fun handle(data: Any) {
                val d = data as ItStarted
                notifier.fireTestStarted(Description.createTestDescription(onName, d.description))
                println(data)
            }
        })
        org.spek.eventAggregator.subscribe("AssertionErrorOccurred", object:EventHandler {
            override fun handle(data: Any) {
                val d = data as AssertionErrorOccurred
                notifier.fireTestAssumptionFailed(Failure(Description.createTestDescription(onName, d.description), d.error))
                println(data)
            }
        })
        org.spek.eventAggregator.subscribe("ItCompleted", object:EventHandler {
            override fun handle(data: Any) {
                val d = data as ItCompleted
                notifier.fireTestFinished(Description.createTestDescription(onName, d.description))
                println(data)
            }
        })

        spek.actions forEach {
            val description = Description.createSuiteDescription(it.Description)

//            notifier.fireTestStarted(description)
            it.run()
//            notifier.fireTestFinished(description)
        }

        notifier.fireTestRunFinished(Result())
    }
}
