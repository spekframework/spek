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
}
