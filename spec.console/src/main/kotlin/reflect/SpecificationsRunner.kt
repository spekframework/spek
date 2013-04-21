package org.spek.console.reflect;

import org.spek.impl.*
import org.spek.impl.events.*
import org.spek.console.reflect.*
import org.spek.console.api.ConsoleSpek
import org.spek.api.skip

public class SpecificationRunner(val listener: Listener) {
    private val BASE_CLASS = javaClass<ConsoleSpek>()
    fun runSpecs(packageName: String) {
        val classes = FileClassLoader.getClasses(BASE_CLASS, packageName)

        classes forEach { specificationClass ->
            if (!BASE_CLASS.isAssignableFrom(specificationClass)) {
                throw RuntimeException("All spec classes should be inherited from $BASE_CLASS")
            }

            var skipped = false
            val annotations = specificationClass.getAnnotations()!!
            for (annotation in annotations) {
                val clazz = annotation!!.annotationType()
                if (clazz == javaClass<skip>()) {
                    skipped = true
                    listener.spek(specificationClass.getName()).executionSkipped("$annotation")
                }
            }
            if (!skipped) {
                val givenActions = (specificationClass.newInstance() as ConsoleSpek).allGivens()
                givenActions forEach { Runner.executeSpec(it, listener) }
            }

            //TODO: this is the correct way, but it seems there is bug in kotlin annotations :(
            //            if (specificationClass.isAnnotationPresent(javaClass<skip>())) {
            //                val skipAnnotation = specificationClass.getAnnotation(javaClass<skip>())
            //                listener.spek(specificationClass.getName()).executionSkipped(skipAnnotation!!.why)
            //            } else {
            //                val givenActions = (specificationClass.newInstance() as ConsoleSpek).allGivens()
            //                givenActions forEach {  Runner.executeSpec(it, listener) }
            //            }
        }
    }
}
