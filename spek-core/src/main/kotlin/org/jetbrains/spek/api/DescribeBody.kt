package org.jetbrains.spek.api

interface DescribeBody  {
    fun describe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun xit(description: String, assertions: () -> Unit)
    fun it(description: String, assertions: () -> Unit)
    fun beforeEach(actions: () -> Unit)
    fun afterEach(actions: () -> Unit)
    fun on(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun given(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun context(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun xon(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
    fun xgiven(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
    fun xcontext(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
}