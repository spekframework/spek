package org.jetbrains.spek.meta

import java.lang.annotation.Inherited

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Experimental
annotation class SpekDsl
