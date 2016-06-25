package org.jetbrains.spek.api.extension

import org.jetbrains.spek.api.annotation.Beta
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
@Beta
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class SpekExtension(val extension: KClass<out Extension>)
