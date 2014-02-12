package org.spek.console

import org.spek.*
import org.spek.impl.*
import org.reflections.*
import java.lang.reflect.*
import org.reflections.util.*
import org.reflections.scanners.*
import org.reflections.adapters.MetadataAdapter

public fun findTestsInPackage(packageName: String): MutableList<TestSpekAction> {
    val result = arrayListOf<TestSpekAction>()

    val reflectionConfig = ConfigurationBuilder
            .build(packageName, SubTypesScanner())!!
            .useParallelExecutor()

    val reflections = Reflections(reflectionConfig);

    result addAll (reflections.getSubTypesOf(javaClass<Spek>())!! map { ClassSpek(it) })

    /*
        val spekClazz = toAnnotationClazz(javaClass<specification>())
        val annotatedMethods = reflections.getMethodsAnnotatedWith(spekClazz)!!

        result addAll (annotatedMethods map { ExtensionFunctionSpek(it) })
    */

    return result
}

public fun findTestsInClass(clazz: Class<*>): MutableList<TestSpekAction> {
    val result = arrayListOf<TestSpekAction>()

    if (javaClass<Spek>().isAssignableFrom(clazz)) {
        result add ClassSpek(clazz as Class<Spek>)
    }

    /*
        result addAll clazz.getMethods()
                .filter { Modifier.isStatic(it.getModifiers()) && hasAnnotationClazz(it, javaClass<specification>()) }
                .map { ExtensionFunctionSpek(it) }
    */

    return result
}

private fun AnnotatedElement.checkSkipped() {
    /*
    * TODO: need to be refactored when #KT-3534, KT-3534 got fixed.
    */
    val skip = getAnnotation(this, javaClass<ignored>())
    if (skip != null) throw SkippedException(skip.why)
}

public data class ExtensionFunctionSpek(val method: Method) : TestSpekAction {
    override fun description(): String = method.toString()

    override fun iterateGiven(it: (TestGivenAction) -> Unit) {
        val builder = object : Spek() {
        }
        //TODO: assert method signature

        method.checkSkipped()
        method.invoke(null, builder)

        builder.iterateGiven(it)
    }
}

public data class ClassSpek<T : Spek>(val specificationClass: Class<out T>) : TestSpekAction {
    override fun description(): String = specificationClass.getName()

    override fun iterateGiven(it: (TestGivenAction) -> Unit) {
        specificationClass.checkSkipped()
        specificationClass.newInstance().iterateGiven(it)
    }
}
