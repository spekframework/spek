package org.spekframework.spek2.junit

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.spekframework.spek2.dsl.Pending
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.scope.GroupScopeImpl
import org.spekframework.spek2.runtime.scope.PathBuilder
import org.spekframework.spek2.runtime.scope.ScopeId
import org.spekframework.spek2.runtime.scope.ScopeType
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
        val path = PathBuilder()
            .append("SomeClass")
            .build()

        val scope = GroupScopeImpl(
            ScopeId(ScopeType.CLASS, "SomeClass"),
            path,
            null,
            Pending.No,
            lifecycleManager
        )

        assertThat(factory.create(scope), sameInstance(factory.create(scope)))
    }
}
