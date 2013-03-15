package org.spek.junit.impl

import org.junit.runner.*
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.spek.api.*
import org.spek.impl.*
import org.spek.junit.api.*

public class JSpec<T>(val clazz : Class<T>) : Runner() {
    public override fun getDescription(): Description? = Description.createSuiteDescription("Spec:" + clazz.getName())

    private fun println(s : Any?) {}

    public override fun run(_notifier: RunNotifier?) {
        val notifier = _notifier!!

        notifier.fireTestRunStarted(getDescription())

        if (!javaClass<Spek>().isAssignableFrom(clazz)) {
            throw RuntimeException("All spec classes should be inherited from ${javaClass<Spek>()}")
        }

        val spek = (clazz.newInstance() as Spek).allGivens()
        spek forEach { given ->
            given.performInit() forEach { on ->
                on.performInit() forEach { it ->
                    var desc = Description.createTestDescription(given.Description() + ": " + on.Description(), it.Description())
                    notifier.fireTestStarted(desc)
                    it.run()
                    notifier.fireTestFinished(desc)
                }
            }
        }
    }
}
