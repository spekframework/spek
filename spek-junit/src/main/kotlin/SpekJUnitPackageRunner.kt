package org.spek.junit.api

import org.junit.runner.RunWith
import org.junit.runner.Runner
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier
import org.spek.reflect.SpecificationRunner
import org.spek.junit.impl.JUnitLogger
import org.junit.Test

public open class SpekJUnitPackageRunner(_ : Class<*>) : Runner() {
    private fun packageName() : String = getClass().getPackage()!!.getName()!!

    val suiteDescription = Description.createSuiteDescription(packageName())!!

    public override fun getDescription(): Description = suiteDescription

    public override fun run(notifier: RunNotifier?) {
        SpecificationRunner(JUnitLogger(notifier!!, suiteDescription)).runSpecs(packageName())
    }

    Test public fun mockTest() {}
}
