package org.jetbrains.spek.api

import org.jetbrains.spek.api.annotation.Beta
import org.jetbrains.spek.api.dsl.SubjectDsl

/**
 * @author Ranie Jade Ramiso
 */
@Beta
abstract class SubjectSpek<T>(val spec: SubjectDsl<T>.() -> Unit)
