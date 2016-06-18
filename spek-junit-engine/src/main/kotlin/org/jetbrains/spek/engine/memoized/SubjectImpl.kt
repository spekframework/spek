package org.jetbrains.spek.engine.memoized

import org.jetbrains.spek.api.memoized.CachingMode
import org.jetbrains.spek.api.memoized.Subject

/**
 * @author Ranie Jade Ramiso
 */
class SubjectImpl<T>(mode: CachingMode, factory: () -> T): Subject<T>, MemoizedHelper<T>(mode, factory)
