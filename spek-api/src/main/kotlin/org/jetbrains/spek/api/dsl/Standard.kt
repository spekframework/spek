package org.jetbrains.spek.api.dsl

import kotlin.reflect.KClass

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.describe(description: String, body: Dsl.() -> Unit) {
    group("describe $description", body = body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.context(description: String, body: Dsl.() -> Unit) {
    group("context $description", body = body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.given(description: String, body: Dsl.() -> Unit) {
    group("given $description", body = body)
}

/**
 * Creates a [test][Dsl.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.it(description: String, body: () -> Unit) {
    test("it $description", body = body)
}

/**
 * Creates a [test][Dsl.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.on(description: String, body: () -> Unit) {
    test("on $description", body = body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xdescribe(description: String, reason: String? = null, body: Dsl.() -> Unit) {
    group("describe $description", Pending.Yes(reason), body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xcontext(description: String, reason: String? = null, body: Dsl.() -> Unit) {
    group("context $description", Pending.Yes(reason), body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xgiven(description: String, reason: String? = null, body: Dsl.() -> Unit) {
    group("given $description", Pending.Yes(reason), body)
}

/**
 * Creates a pending [test][Dsl.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xit(description: String, reason: String? = null, body: () -> Unit = {}) {
    test("it $description", Pending.Yes(reason), body)
}

/**
 * Creates a pending [test][Dsl.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xon(description: String, reason: String? = null, body: () -> Unit = {}) {
    test("on $description", Pending.Yes(reason), body)
}
