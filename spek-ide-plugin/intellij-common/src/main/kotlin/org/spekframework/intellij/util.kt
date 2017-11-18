package org.spekframework.intellij

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.refactoring.isAbstract
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

private val SPEK_CLASSES = listOf(
    "org.jetbrains.spek.api.Spek",
    "org.spekframework.spek2.Spek"
)

fun extractPath(element: PsiElement): Path? {
    var path: Path? = null
    when (element) {
        is KtClassOrObject -> {
            if (isSpekSubclass(element) && isSpekRunnable(element)) {
                val fqName = element.fqName
                if (fqName != null) {
                    val pkg = fqName.parent().asString()
                    val cls = fqName.shortName().asString()
                    path = PathBuilder()
                        .append(pkg)
                        .append(cls)
                        .build()
                }

            }
        }
    }

    return path
}

/**
 * Recursively check the type hierarchy until a match is found
 */
fun isSpekSubclass(element: KtClassOrObject): Boolean {
    val superTypeCallEntry = element.superTypeListEntries.filterIsInstance<KtSuperTypeCallEntry>()
        .firstOrNull()

    if (superTypeCallEntry != null) {
        val constructorReference = superTypeCallEntry.calleeExpression.constructorReferenceExpression
        if (constructorReference != null) {
            val resolved = constructorReference.mainReference.resolve()

            if (resolved != null && resolved is KtPrimaryConstructor) {
                val cls = resolved.getContainingClassOrObject()
                val fqName = cls.fqName
                return if (fqName != null && SPEK_CLASSES.contains(fqName.toString())) {
                    true
                } else {
                    isSpekSubclass(cls)
                }
            }

        }
    }

    return false
}

fun isSpekRunnable(element: KtClassOrObject) = element.isTopLevel() && !element.isAbstract()
