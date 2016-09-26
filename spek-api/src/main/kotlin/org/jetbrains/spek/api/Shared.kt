package org.jetbrains.spek.api

import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
fun Spec.include(spec: Spek) {
    spec.spec(this)
}
