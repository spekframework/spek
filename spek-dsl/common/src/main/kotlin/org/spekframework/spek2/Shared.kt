package org.spekframework.spek2

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.meta.Experimental

/**
 * @since 1.1
 */
@Experimental
fun Root.include(spec: Spek) {
    spec.root(this)
}
