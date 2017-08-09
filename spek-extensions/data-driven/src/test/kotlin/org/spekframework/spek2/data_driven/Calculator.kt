package org.spekframework.spek2.data_driven;

class Calculator {
    fun add(x: Int, y: Int) = x + y
    fun subtract(x: Int, y: Int) = x - y
    fun divide(x: Int, y: Int): Int {
        if (y == 0) {
            throw IllegalArgumentException()
        }
        return x / y
    }
}
