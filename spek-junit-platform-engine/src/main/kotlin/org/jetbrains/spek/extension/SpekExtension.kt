package org.jetbrains.spek.extension

import org.jetbrains.spek.meta.Experimental
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class SpekExtension(val extension: KClass<out Extension>)
