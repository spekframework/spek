package org.spekframework.spek2

import org.spekframework.spek2.dsl.Root

/**
 * @since 1.0
 */
abstract class Spek(val root: Root.() -> Unit) {
    companion object {
        fun wrap(root: Root.() -> Unit) = @Ignore object : Spek(root) {}
    }
}
