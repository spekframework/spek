package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.meta.Experimental
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class SpekExtension(val extension: KClass<out Extension>)
