package org.spekframework.spek2

import org.spekframework.spek2.lifecycle.InstanceFactory
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CreateWith(val factory: KClass<out InstanceFactory>)
