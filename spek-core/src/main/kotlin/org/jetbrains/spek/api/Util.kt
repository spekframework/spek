package org.jetbrains.spek.api

fun <T> removingIterator(data: MutableIterable<T>, each: (T) -> Unit): Unit {
    val it = data.iterator()
    while (it.hasNext()) {
        each(it.next())
        it.remove()
    }
}