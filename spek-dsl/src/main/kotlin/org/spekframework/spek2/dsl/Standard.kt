package org.spekframework.spek2.dsl

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.describe(description: String, body: SpecBody.() -> Unit) {
    group("describe $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.context(description: String, body: SpecBody.() -> Unit) {
    group("context $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.given(description: String, body: SpecBody.() -> Unit) {
    group("given $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.on(description: String, body: ActionBody.() -> Unit) {
    action("on $description", body = body)
}

/**
 * Creates a [test][SpecBody.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun TestContainer.it(description: String, body: TestBody.() -> Unit) {
    test("it $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.xdescribe(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
    group("describe $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.xcontext(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
    group("context $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.xgiven(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
    group("given $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a pending [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun SpecBody.xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
    action("on $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a pending [test][SpecBody.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun TestContainer.xit(description: String, reason: String? = null, body: TestBody.() -> Unit = {}) {
    test("it $description", Pending.Yes(reason), body)
}
