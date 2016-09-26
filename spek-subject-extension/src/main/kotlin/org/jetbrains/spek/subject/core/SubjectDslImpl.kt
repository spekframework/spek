package org.jetbrains.spek.subject.core

import org.jetbrains.spek.api.dsl.Spec
import org.jetbrains.spek.subject.dsl.SubjectDsl

/**
 * @author Ranie Jade Ramiso
 */
internal abstract class SubjectDslImpl<T>(val root: Spec): SubjectDsl<T>, Spec by root
