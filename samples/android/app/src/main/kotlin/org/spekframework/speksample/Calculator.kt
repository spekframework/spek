package org.spekframework.speksample

class Calculator {
    fun add(x: Int, y: Int) = x + y

    fun subtract(x: Int, y: Int) = x - y

    fun mul(x: Int, y: Int) = x * y

    fun div(x: Int, y: Int) = if (y != 0) x / y
}
