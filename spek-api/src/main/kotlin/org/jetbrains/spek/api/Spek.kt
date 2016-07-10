package org.jetbrains.spek.api

import org.jetbrains.spek.api.dsl.Dsl

/**
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
abstract class Spek(val spec: Dsl.() -> Unit)
