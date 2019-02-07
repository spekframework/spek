package org.spekframework.spek2.runtime.util

import kotlin.reflect.KClass

object ClassUtil {
    fun extractPackageAndClassNames(clz: KClass<*>): Pair<String, String> {
        val qualifiedName = checkNotNull(clz.qualifiedName)
        val className = checkNotNull(clz.simpleName)
        var packageName = qualifiedName.removeSuffix(className)
            .trimEnd('.')

        return packageName to className
    }
}