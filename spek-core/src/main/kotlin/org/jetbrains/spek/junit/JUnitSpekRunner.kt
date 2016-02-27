package org.jetbrains.spek.junit

import org.jetbrains.spek.api.Spek
import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.notification.RunNotifier

class JUnitSpekRunner(val specificationClass: Class<*>) : Runner() {
    private val junitDescriptionCache = JUnitDescriptionCache()

    private fun spek(): Spek? = _children

    val _children by lazy(LazyThreadSafetyMode.NONE) {
        if (Spek::class.java.isAssignableFrom(specificationClass) && !specificationClass.isLocalClass) {
            specificationClass.newInstance() as Spek
        } else {
            null
        }
    }

    override fun getDescription(): Description? {
        return junitDescriptionCache.get(spek()!!.testAction())
    }

    override fun run(junitNotifier: RunNotifier?) {
        spek()?.run(JUnitNotifier(junitNotifier!!, junitDescriptionCache))
    }
}

