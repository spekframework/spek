package org.jetbrains.spek.api.dsl

import org.jetbrains.spek.api.SubjectSpek
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
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.on(description: String, body: Dsl.() -> Unit) {
    group("on $description", lazy = true, body = body)
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
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xdescribe(description: String, reason: String? = null, body: Dsl.() -> Unit) {
    group("describe $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xcontext(description: String, reason: String? = null, body: Dsl.() -> Unit) {
    group("context $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xgiven(description: String, reason: String? = null, body: Dsl.() -> Unit) {
    group("given $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a pending [group][Dsl.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun Dsl.xon(description: String, reason: String? = null, body: Dsl.() -> Unit = {}) {
    group("on $description", Pending.Yes(reason), lazy = true, body = body)
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
 * Alias for [SubjectDsl.includeSubjectSpec].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
fun <T, K: SubjectSpek<T>> SubjectDsl<*>.itBehavesLike(spec: KClass<K>) {
    includeSubjectSpec(spec)
}
