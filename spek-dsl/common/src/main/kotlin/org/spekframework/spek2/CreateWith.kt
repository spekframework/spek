package org.spekframework.spek2

import org.spekframework.spek2.lifecycle.InstanceFactory
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CreateWith(val factory: KClass<out InstanceFactory>)
