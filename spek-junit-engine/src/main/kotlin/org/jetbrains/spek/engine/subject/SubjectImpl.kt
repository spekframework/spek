package org.jetbrains.spek.engine.subject

import org.jetbrains.spek.engine.memoized.MemoizedHelper
import org.jetbrains.spek.subject.Subject

/**
 * @author Ranie Jade Ramiso
 */
class SubjectImpl<T>(factory: () -> T): Subject<T>, MemoizedHelper<T>(factory)
