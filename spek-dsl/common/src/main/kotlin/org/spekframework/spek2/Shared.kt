package org.spekframework.spek2

import org.spekframework.spek2.dsl.Spec
import org.spekframework.spek2.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 * @since 1.1
 */
@Experimental
fun Spec.include(spec: Spek) {
    spec.spec(this)
}
