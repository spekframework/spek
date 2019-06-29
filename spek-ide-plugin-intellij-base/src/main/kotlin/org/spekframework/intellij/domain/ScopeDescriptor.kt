package org.spekframework.intellij.domain

import org.jetbrains.kotlin.psi.KtElement
import org.spekframework.spek2.runtime.scope.Path

sealed class ScopeDescriptor(val path: Path, val element: KtElement,
                             val excluded: Boolean, val runnable: Boolean) {
    class Group(path: Path, element: KtElement, excluded: Boolean, runnable: Boolean,
                val children: List<ScopeDescriptor>): ScopeDescriptor(path, element, excluded, runnable) {
        fun findDescriptorForElement(element: KtElement): ScopeDescriptor? {
            return findMatching(this, element)
        }

        private fun findMatching(current: ScopeDescriptor, element: KtElement): ScopeDescriptor? {
            if (current.element == element) {
                return current
            } else if (current is ScopeDescriptor.Group) {
                for (child in current.children) {
                    val descriptor = findMatching(child, element)
                    if (descriptor != null) {
                        return descriptor
                    }
                }
            }
            return null
        }
    }
    class Test(path: Path, element: KtElement,
               excluded: Boolean, runnable: Boolean): ScopeDescriptor(path, element, excluded, runnable)
}