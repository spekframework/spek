package org.jetbrains.spek.api

interface DescribeBody {
    fun describe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun xit(description: String, assertions: () -> Unit)
    fun it(description: String, assertions: () -> Unit)
    fun beforeEach(actions: () -> Unit)
    fun afterEach(actions: () -> Unit)
}