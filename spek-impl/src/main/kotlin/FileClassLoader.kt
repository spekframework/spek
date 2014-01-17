package org.spek.reflect;

import org.reflections.Reflections
import org.spek.impl.TestGivenAction
import org.spek.api.annotations.skip
import org.spek.impl.SkippedException
import org.spek.impl.SpekImpl
import java.lang.reflect.Method
import java.lang.reflect.AnnotatedElement
import org.spek.api.annotations.spek
import org.spek.impl.AnnotationsHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import java.lang.reflect.Modifier
import org.spek.impl.TestFixtureAction

/**
 * @author hadihariri, jonnyzzz
 */
public object FileClassLoader {
    public fun findTestsInPackage(packageName : String ) : List<TestFixtureAction>{
        val result = arrayListOf<TestFixtureAction>()

        val reflectionConfig = ConfigurationBuilder.build(packageName)!!
        reflectionConfig.addScanners(SubTypesScanner())
        reflectionConfig.addScanners(MethodAnnotationsScanner())
        reflectionConfig.useParallelExecutor()
        val reflections = Reflections(reflectionConfig);

        result addAll (reflections.getSubTypesOf(javaClass<SpekImpl>())!! map { ClassSpek(it) })

        val spekClazz = AnnotationsHelper.toAnnotationClazz(javaClass<spek>())
        val annotatedMethods = reflections.getMethodsAnnotatedWith(spekClazz)!!

        result addAll (annotatedMethods map { ExtensionFunctionSpek(it)})

        return result
    }

    public fun findTestsInClass(clazz : Class<*>) : List<TestFixtureAction>{
        val result = arrayListOf<TestFixtureAction>()

        if (javaClass<SpekImpl>().isAssignableFrom(clazz)) {
            result add ClassSpek(clazz as Class<SpekImpl>)
        }

        result addAll clazz.getMethods()
                .filter { Modifier.isStatic(it.getModifiers()) &&  AnnotationsHelper.hasAnnotationClazz(it, javaClass<spek>()) }
                .map { ExtensionFunctionSpek(it)}

        return result
    }
}

private fun AnnotatedElement.checkSkipped() {
    /*
    * TODO: need to be refactored when #KT-3534, KT-3534 got fixed.
    */
    val skip = AnnotationsHelper.getAnnotation(this, javaClass<skip>())
    if (skip != null) throw SkippedException(skip.value() ?: "")
}

public data class ExtensionFunctionSpek(val method : Method) : TestFixtureAction {
    override fun description(): String = method.toString()
    override fun allGiven(): List<TestGivenAction> {
        val builder = SpekImpl()
        //TODO: assert method signature

        method.checkSkipped()
        method.invoke(null, builder)

        return builder.allGiven()
    }
}

public data class ClassSpek<T : SpekImpl>(val specificationClass: Class<out T>) : TestFixtureAction {
    override fun description(): String = specificationClass.toString()
    override fun allGiven(): List<TestGivenAction> {
        specificationClass.checkSkipped()
        return specificationClass.newInstance().allGiven()
    }
}
