package org.spekframework.spek2.dsl

import org.spekframework.spek2.lifecycle.MemoizedValue

interface ScopeBody {
    fun <T> memoized(): MemoizedValue<T>
}
