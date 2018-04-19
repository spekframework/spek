package org.spekframework.spek2.dsl

import org.spekframework.spek2.meta.SpekDsl

@SpekDsl
interface ActionBody: TestContainer {

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun beforeEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun afterEachTest(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun beforeGroup(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun afterGroup(callback: () -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun action(description: String, pending: Pending = Pending.No, body: ActionBody.() -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun on(description: String, body: ActionBody.() -> Unit) {
        throw UnsupportedOperationException()
    }

    @Deprecated(message = "Can't be used within action scopes", level = DeprecationLevel.ERROR)
    fun xon(description: String, reason: String? = null, body: ActionBody.() -> Unit = {}) {
        throw UnsupportedOperationException()
    }
}
