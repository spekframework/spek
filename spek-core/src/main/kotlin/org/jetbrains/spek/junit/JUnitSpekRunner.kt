package org.jetbrains.spek.junit

import org.jetbrains.spek.api.Spek
import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.notification.RunNotifier

class JUnitSpekRunner(val specificationClass: Class<*>) : Runner() {
    private val junitDescriptionCache = JUnitDescriptionCache()
    val spek: Spek

    init {
        if (Spek::class.java.isAssignableFrom(specificationClass) && !specificationClass.isLocalClass) {
            spek = specificationClass.newInstance() as Spek
        } else {
            throw RuntimeException(specificationClass.canonicalName + " must be a subclass of "
                    + Spek::class.qualifiedName
                    + " in order to use " + JUnitSpekRunner::class.simpleName)
        }
    }

    override fun getDescription(): Description? {
        return junitDescriptionCache.get(spek.tree)
    }

    override fun run(junitNotifier: RunNotifier?) {
        spek.run(JUnitNotifier(junitNotifier!!, junitDescriptionCache))
    }
}
