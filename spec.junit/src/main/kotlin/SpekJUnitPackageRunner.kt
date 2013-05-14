package org.spek.junit.api

import org.junit.runner.RunWith
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.spek.console.reflect.SpecificationRunner
import org.spek.junit.impl.JUnitLogger
import org.junit.Test

public open class SpekJUnitPackageRunner(_ : Class<*>) : Runner() {
    private fun packageName() : String = getClass().getPackage()!!.getName()!!

    public override fun getDescription(): Description? = Description.createSuiteDescription(packageName())

    public override fun run(notifier: RunNotifier?) {
        SpecificationRunner(JUnitLogger(notifier!!)).runSpecs(packageName())
    }

    Test public fun mockTest() {}
}
