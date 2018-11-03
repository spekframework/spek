package org.spekframework.intellij.structure

import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry
import org.spekframework.spek2.runtime.scope.Path

class TestStructure {
    fun findScopeForElement(element: KtElement): Path {
        TODO()
    }
}

object SpekTestStructureCache {
    private val SPEK_CLASSES = listOf(
        "org.spekframework.spek2.Spek"
    )

    fun getSpekTestStructure(clz: KtClassOrObject): TestStructure? {
        if (!isSpekSubclass(clz)) {
            return null
        }

        clz.superTypeListEntries

        TODO()
    }

    fun isSpekSubclass(element: KtClassOrObject): Boolean {
        val superClass = org.spekframework.intellij.getSuperClass(element)
        val fqName = superClass?.getKotlinFqName()
        if (fqName != null) {
            if (SPEK_CLASSES.contains(fqName.toString())) {
                return true
            } else {
                return isSpekSubclass(superClass)
            }
        }
        return false
    }

    private fun getSuperClass(element: KtClassOrObject): KtClassOrObject? {
        val superTypes = element.superTypeListEntries
        var superClass: KtClassOrObject? = null

        for (entry in superTypes) {
            if (entry is KtSuperTypeCallEntry) {
                val ref = entry.calleeExpression.constructorReferenceExpression
                    ?.mainReference
                    ?.resolve()

                if (ref is KtPrimaryConstructor) {
                    superClass = ref.getContainingClassOrObject()
                    break
                }
            }
        }

        return superClass
    }
}