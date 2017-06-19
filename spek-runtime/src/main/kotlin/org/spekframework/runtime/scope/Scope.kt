package org.spekframework.runtime.scope

import org.jetbrains.spek.api.dsl.ActionBody
import org.jetbrains.spek.api.dsl.TestBody

sealed class Scope {
    abstract class Parent<T: Scope>: Scope() {
        private val children = mutableListOf<T>()

        fun addChild(child: T) {
            children.add(child)
        }

        fun getChildren() = children.toList()
    }

    class Group: Parent<Scope>()
    class Action(val body: ActionBody.() -> Unit): Parent<Test>()
    class Test(val body: TestBody.() -> Unit): Scope()
}
