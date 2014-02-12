package org.spek.impl

import org.spek.*

fun removingIterator<T>(data: MutableIterable<T>, each: (T) -> Unit): Unit {
    val it = data.iterator()
    while (it.hasNext()) {
        each(it.next())
        it.remove()
    }
}