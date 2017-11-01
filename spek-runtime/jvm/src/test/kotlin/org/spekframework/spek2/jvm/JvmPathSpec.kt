package org.spekframework.spek2.jvm

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.runtime.scope.PathBuilder

class JvmPathSpec {

    @Test
    fun create() {
        val path = PathBuilder()
            .append("com.example.com")
            .append("SomeClass")
            .build()

        assertThat(path.name, equalTo("SomeClass"))
        assertThat(path.parent!!.name, equalTo("com.example.com"))
    }

    @Test
    fun root() {
        val path = PathBuilder()
            .append("com.example.com")
            .append("SomeClass")
            .build()

        assertThat(path.parent!!.parent!!.name, equalTo(""))
    }

    @Test
    fun createFromClass() {
        val path = PathBuilder.from(Spek::class)
            .build()

        assertThat(path.name, equalTo(Spek::class.simpleName))
        assertThat(path.parent!!.name, equalTo(Spek::class.java.`package`.name))
    }

    @Test
    fun parse() {
        val path = PathBuilder()
            .append("com.example.com")
            .append("SomeClass")
            .build()

        val result = PathBuilder.parse(path.serialize())
            .build()

        assertThat(path, equalTo(result))
    }
}
