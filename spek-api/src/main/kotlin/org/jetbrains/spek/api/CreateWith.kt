package org.jetbrains.spek.api

import org.jetbrains.spek.api.lifecycle.InstanceFactory
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class CreateWith(val factory: KClass<out InstanceFactory>)
