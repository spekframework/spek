package org.jetbrains.spek.console

import java.lang.reflect.AnnotatedElement

public fun <T : Annotation> getAnnotation(element: AnnotatedElement, clazz: Class<T>): T? {
    //Dirty workaround for KT-3534
    if (!javaClass<Annotation>().isAssignableFrom(clazz))
        throw RuntimeException("$clazz must be an annotation")
    val annotation = element.getAnnotation(clazz) ?: return null
    return clazz.cast(annotation)
}

public fun <T> toAnnotationClazz(clazz: Class<T>): Class<out Annotation> {
    //Dirty workaround for KT-3534
    if (!javaClass<Annotation>().isAssignableFrom(clazz))
        throw RuntimeException("$clazz must be an annotation")
    //noinspection unchecked
    return clazz as Class<out Annotation>
}

public fun <T> hasAnnotationClazz(element: AnnotatedElement, clazz: Class<T>): Boolean {
    //Dirty workaround for KT-3534
    if (!javaClass<Annotation>().isAssignableFrom(clazz))
        throw RuntimeException("$clazz must be an annotation")
    //noinspection unchecked

    return element.isAnnotationPresent(clazz as Class<out Annotation>)
}
