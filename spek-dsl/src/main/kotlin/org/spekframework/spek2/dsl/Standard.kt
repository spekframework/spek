package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.Synonym
import org.spekframework.spek2.meta.SynonymType

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Group, prefix = "describe ")
fun SpecBody.describe(description: String, body: SpecBody.() -> Unit) {
    group("describe $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Group, prefix = "context ")
fun SpecBody.context(description: String, body: SpecBody.() -> Unit) {
    group("context $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Group, prefix = "given ")
fun SpecBody.given(description: String, body: SpecBody.() -> Unit) {
    group("given $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Action, prefix = "on ")
fun SpecBody.on(description: String, body: ActionBody.() -> Unit) {
    action("on $description", body = body)
}

/**
 * Creates a [test][SpecBody.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Test, prefix = "it ")
fun TestContainer.it(description: String, body: TestBody.() -> Unit) {
    test("it $description", body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Group, prefix = "describe ", excluded = true)
fun SpecBody.xdescribe(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
    group("describe $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Group, prefix = "context ", excluded = true)
fun SpecBody.xcontext(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
    group("context $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Group, prefix = "given ", excluded = true)
fun SpecBody.xgiven(description: String, reason: String? = null, body: SpecBody.() -> Unit) {
    group("given $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a pending [group][SpecBody.group].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Action, prefix = "on ", excluded = true)
fun SpecBody.xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
    action("on $description", Pending.Yes(reason), body = body)
}

/**
 * Creates a pending [test][SpecBody.test].
 *
 * @author Ranie Jade Ramiso
 * @since 1.0
 */
@Synonym(type = SynonymType.Test, prefix = "it ", excluded = true)
fun TestContainer.xit(description: String, reason: String? = null, body: TestBody.() -> Unit = {}) {
    test("it $description", Pending.Yes(reason), body)
}
