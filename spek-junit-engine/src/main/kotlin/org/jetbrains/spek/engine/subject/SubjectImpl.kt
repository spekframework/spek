package org.jetbrains.spek.engine.subject

import org.jetbrains.spek.api.subject.Subject
import org.jetbrains.spek.engine.memoized.MemoizedHelper

/**
 * @author Ranie Jade Ramiso
 */
class SubjectImpl<T>(factory: () -> T): Subject<T>, MemoizedHelper<T>(factory)
