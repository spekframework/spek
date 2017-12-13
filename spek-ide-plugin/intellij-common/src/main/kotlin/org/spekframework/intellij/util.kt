package org.spekframework.intellij

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.toLightAnnotation
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.idea.refactoring.isAbstract
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.jetbrains.kotlin.psi.psiUtil.referenceExpression
import org.jetbrains.uast.java.annotations
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

private val SPEK_CLASSES = listOf(
    "org.spekframework.spek2.Spek"
)

private val SYNONYM_CLASSES = listOf(
    "org.spekframework.spek2.meta.Synonym"
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
        is PsiDirectory -> {
            val pkg = element.getPackage()
            if (pkg != null) {
                path = PathBuilder()
                    .append(pkg.qualifiedName)
                    .build()
            }
        }
        is KtCallExpression -> {
             path = extractPath(element)
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

// TODO: check for @Ignore
fun isSpekRunnable(element: KtClassOrObject) = element.isTopLevel() && !element.isAbstract()

private fun extractPath(callExpression: KtCallExpression): Path? {
    val calleeExpression = callExpression.calleeExpression
    if (calleeExpression != null) {
        val mainReference = calleeExpression.mainReference
        if (mainReference != null) {
            val resolved = mainReference.resolve()
            if (resolved != null && resolved is KtNamedFunction) {
                extractSynonymAnnotation(resolved)
            }
        }
    }
    /// PsiTreeUtil.getParentOfType(element, type, true)
    return null
}

private fun extractSynonymAnnotation(function: KtNamedFunction) {
    val lightMethod = function.toLightMethods().firstOrNull()
    val annotation = lightMethod?.annotations?.find {
        SYNONYM_CLASSES.contains(it.qualifiedName)
    }

    if (annotation != null) {
        val type = annotation.findDeclaredAttributeValue("type")
        val prefix = annotation.findDeclaredAttributeValue("prefix")
        val excluded = annotation.findDeclaredAttributeValue("excluded")

        println("Synonym(type=${type?.text}, prefix=${prefix?.text}, excluded=${excluded?.text})")
    }
}
