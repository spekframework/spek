package org.spekframework.runtime.scope

import org.jetbrains.spek.api.dsl.ActionBody
import org.jetbrains.spek.api.dsl.TestBody

sealed class Scope(val path: Path) {
    abstract class Parent<T: Scope>(path: Path): Scope(path) {
        private val children = mutableListOf<T>()

        fun addChild(child: T) {
            children.add(child)
        }

        fun getChildren() = children.toList()
    }
}

class Group(path: Path): Scope.Parent<Scope>(path)
class Action(path: Path, val body: ActionBody.() -> Unit): Scope.Parent<Test>(path)
class Test(path: Path, val body: TestBody.() -> Unit): Scope(path)
