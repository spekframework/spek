package org.spek.junit.impl

import org.junit.runner.*
import org.junit.runner.notification.*
import org.spek.console.reflect.SpecificationRunner

public class SpekJUnitClassRunner<T>(val specificationClass: Class<T>): Runner() {
    private val rootDescription = Description.createSuiteDescription(specificationClass)!!
    public override fun getDescription(): Description? = rootDescription

    public override fun run(notifier: RunNotifier?) {
        SpecificationRunner(JUnitLogger(notifier!!)).runSpecs(specificationClass)
    }
}
