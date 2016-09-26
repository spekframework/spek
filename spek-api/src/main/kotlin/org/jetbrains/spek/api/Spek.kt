package org.jetbrains.spek.api

import org.jetbrains.spek.api.dsl.Spec

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
abstract class Spek(val spec: Spec.() -> Unit) {
    companion object {
        fun wrap(spec: Spec.() -> Unit) = object: Spek(spec) {}
    }
}
