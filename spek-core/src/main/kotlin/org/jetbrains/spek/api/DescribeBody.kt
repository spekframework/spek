package org.jetbrains.spek.api

interface DescribeBody  {
    fun describe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun xdescribe(description: String, evaluateBody: DescribeBody.() -> Unit)
    fun fdescribe(description: String, evaluateBody: DescribeBody.() -> Unit)

    fun it(description: String, assertions: () -> Unit)
    fun xit(description: String, assertions: () -> Unit)
    fun fit(description: String, assertions: () -> Unit)

    fun beforeEach(actions: () -> Unit)
    fun afterEach(actions: () -> Unit)

    fun on(description: String, evaluateBody: DescribeBody.() -> Unit) = describe("on $description", evaluateBody)
    fun given(description: String, evaluateBody: DescribeBody.() -> Unit) = describe("given $description", evaluateBody)
    fun context(description: String, evaluateBody: DescribeBody.() -> Unit) = describe(description, evaluateBody)
    fun xon(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe("on $description", evaluateBody)
    fun xgiven(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe("given $description", evaluateBody)
    fun xcontext(description: String, evaluateBody: DescribeBody.() -> Unit) = xdescribe(description, evaluateBody)
    fun fon(description: String, evaluateBody: DescribeBody.() -> Unit) = fdescribe("on $description", evaluateBody)
    fun fgiven(description: String, evaluateBody: DescribeBody.() -> Unit) = fdescribe("given $description", evaluateBody)
    fun fcontext(description: String, evaluateBody: DescribeBody.() -> Unit) = fdescribe(description, evaluateBody)
}