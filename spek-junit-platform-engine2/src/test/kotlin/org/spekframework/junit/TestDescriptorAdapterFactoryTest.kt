package org.spekframework.junit

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.dsl.Pending
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.spekframework.jvm.JvmPath
import org.spekframework.runtime.lifecycle.LifecycleManager
import org.spekframework.runtime.scope.GroupScopeImpl
import org.spekframework.runtime.scope.ScopeId
import kotlin.properties.Delegates

class TestDescriptorAdapterFactoryTest {

    var factory: TestDescriptorAdapterFactory by Delegates.notNull()
    var lifecycleManager: LifecycleManager by Delegates.notNull()


    @BeforeEach
    fun setup() {
        factory = TestDescriptorAdapterFactory()
        lifecycleManager = mock()
    }

    @Test
    fun caching() {
        val scope = GroupScopeImpl(
            ScopeId("class", "SomeClass"),
            JvmPath.from(("SomeClass")),
            null,
            Pending.No,
            lifecycleManager
        )

        assertThat(factory.create(scope), sameInstance(factory.create(scope)))
    }
}
