package org.jetbrains.spek

import org.jetbrains.spek.dsl.Dsl

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
abstract class Spek(val spec: Dsl.() -> Unit)
