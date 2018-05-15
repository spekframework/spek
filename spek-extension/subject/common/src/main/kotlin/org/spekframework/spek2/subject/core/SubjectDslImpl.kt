package org.spekframework.spek2.subject.core

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.subject.dsl.SubjectDsl

internal abstract class SubjectDslImpl<T>(val root: Root): SubjectDsl<T>, Root by root
