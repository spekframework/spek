package org.spekframework.spek2

import org.spekframework.spek2.dsl.Spec

/**
 * @since 1.0
 */
abstract class Spek(val spec: Spec.() -> Unit) {
    companion object {
        fun wrap(spec: Spec.() -> Unit) = @Ignore object: Spek(spec) {}
    }
}
