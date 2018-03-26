package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.SpekDsl

@SpekDsl
interface TestBody {
    @Deprecated(message = "Can't be used within test scopes", level = DeprecationLevel.ERROR)
    fun test(description: String, pending: Pending = Pending.No, body: TestBody.() -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within test scopes", level = DeprecationLevel.ERROR)
    fun it(description: String, body: TestBody.() -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within test scopes", level = DeprecationLevel.ERROR)
    fun xit(description: String, reason: String? = null, body: TestBody.() -> Unit = {}) {
        throw UnsupportedOperationException()
    }
}
