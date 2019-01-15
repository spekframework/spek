package org.spekframework.spek2.meta

/**
 * Any public API with this annotation is considered to be experimental. It can be subject
 * to incompatible changes, or even removal, in future releases.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY
)
annotation class Experimental
