package org.jetbrains.spek.api

import org.jetbrains.spek.api.dsl.SubjectDsl
import org.jetbrains.spek.meta.Experimental

/**
 * @author Ranie Jade Ramiso
 */
@Experimental
abstract class SubjectSpek<T>(val spec: SubjectDsl<T>.() -> Unit)
