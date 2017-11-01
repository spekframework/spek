package org.spekframework.spek2.subject.core

import org.spekframework.spek2.dsl.Spec
import org.spekframework.spek2.subject.dsl.SubjectDsl

/**
 * @author Ranie Jade Ramiso
 */
internal abstract class SubjectDslImpl<T>(val root: Spec): SubjectDsl<T>, Spec by root
