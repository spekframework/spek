package org.spekframework.intellij

import com.intellij.lang.jvm.JvmModifier
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import org.jetbrains.kotlin.asJava.builder.toLightClassOrigin
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.caches.resolve.util.getJavaOrKotlinMemberDescriptor
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.refactoring.pullUp.getSuperTypeEntryByDescriptor
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.lexer.KtToken
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames
import org.jetbrains.kotlin.resolve.lazy.data.KtClassInfoUtil
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

private val SPEK_CLASSES = listOf(
    "org.spekframework.spek2.Spek"
)

private val SYNONYM_CLASSES = listOf(
    "org.spekframework.spek2.meta.Synonym"
)

private val DESCRIPTIONS_CLASSES = listOf(
    "org.spekframework.spek2.meta.Descriptions"
)

/**
 * Marker exception for unsupported operations, we don't propagate this exceptions
 * so that the IDE will not explode.
 */
class UnsupportedFeatureException(message: String): Throwable(message)

fun extractPath(element: PsiElement, searchNearestAlternative: Boolean = false): Path? {
    return when {
        element is PsiDirectory -> {
            val pkg = element.getPackage()
            if (pkg != null) {
                PathBuilder()
                    .append(pkg.qualifiedName)
                    .build()
            } else {
                null
            }
        }
        isIdentifier(element) -> {
            val parent = element.parent
            when (parent) {
                is KtNameReferenceExpression -> extractPath(parent.parent)
                is KtClassOrObject -> extractPath(parent)
                else -> null
            }
        }
        element is KtCallExpression -> extractPathFromCallExpression(element)
        element is KtClassOrObject -> {
            pathBuilderFromKtClassOrObject(element.toLightClass())?.build()
        }
        else -> {
            if (searchNearestAlternative && isInKotlinFile(element)) {
                var nearestCallExpression = PsiTreeUtil.getParentOfType(element, KtCallExpression::class.java)
                if (nearestCallExpression != null) {
                    return extractPath(nearestCallExpression)
                }
            }

            null
        }
    }
}

fun isInKotlinFile(element: PsiElement): Boolean {
    val file = PsiTreeUtil.getParentOfType(element, KtFile::class.java)
    return file != null
}

/**
 * Recursively check the type hierarchy until a match is found
 */
fun isSpekSubclass(element: KtLightClass?): Boolean {
    if (element != null) {
        val superClass = element.superClass

        if (superClass != null && superClass is KtLightClass) {
            val fqName = superClass.getKotlinFqName()
            return if (fqName != null && SPEK_CLASSES.contains(fqName.toString())) {
                true
            } else {
                isSpekSubclass(superClass)
            }
        }
    }

    return false
}

// TODO: check for @Ignore
fun isSpekRunnable(element: KtLightClass) = element.containingClass == null && !element.hasModifier(JvmModifier.ABSTRACT)

private fun extractPathFromCallExpression(callExpression: KtCallExpression, buffer: List<String> = emptyList()): Path? {
    val calleeExpression = callExpression.calleeExpression
    if (calleeExpression != null) {
        val mainReference = calleeExpression.mainReference
        if (mainReference != null) {
            val resolved = mainReference.resolve()
            if (resolved != null && resolved is KtNamedFunction) {
                val synonymContext = extractSynonymContext(resolved)
                if (synonymContext != null && !synonymContext.isExcluded()) {
                    try {
                        val description = synonymContext.constructDescription(callExpression)
                        val parentCallExpression = getParentScopeCallExpression(callExpression)
                        if (parentCallExpression != null) {
                            val newBuffer = mutableListOf<String>()
                            newBuffer.add(description)
                            newBuffer.addAll(buffer)
                            return extractPathFromCallExpression(parentCallExpression, newBuffer)
                        } else {
                            // probably root scope
                            val ktClassOrObject = getRootScopeClassOrObject(callExpression)
                            if (ktClassOrObject != null) {
                                val builder = pathBuilderFromKtClassOrObject(ktClassOrObject.toLightClass())
                                if (builder != null) {
                                    builder.append(description)
                                    buffer.forEach {
                                        builder.append(it)
                                    }

                                    return builder.build()
                                }
                            }
                        }
                    } catch (e: UnsupportedFeatureException) {
                        LOG.warn("Unsupported feature.", e)
                    }
                }

            }
        }
    }
    return null
}


private fun extractSynonymContext(function: KtNamedFunction): SynonymContext? {
    val lightMethod = function.toLightMethods().firstOrNull()
    if (lightMethod != null) {
        val synonym = lightMethod.annotations
            .filter { SYNONYM_CLASSES.contains(it.qualifiedName) }
            .map(::PsiSynonym)
            .firstOrNull()

        if (synonym != null) {
            val descriptions = lightMethod.annotations
                .filter { DESCRIPTIONS_CLASSES.contains(it.qualifiedName) }
                .map(::PsiDescriptions)
                .firstOrNull()

            if (descriptions != null) {
                return SynonymContext(synonym, descriptions)
            }
        }
    }
    return null
}

/**
 * Call the parent of the current scope. Current scope is represented by [callExpression]
 * <code>
 *     group("some group") {
 *          test("test") { .. }
 *     }
 * </code>
 *
 * Current scope here is `test` and what we want to retrieve is CallExpression of `group`.
 *
 */
private fun getParentScopeCallExpression(callExpression: KtCallExpression): KtCallExpression? {
    val block = callExpression.parent
    if (block is KtBlockExpression) {
        val functionLiteral = block.parent
        if (functionLiteral is KtFunctionLiteral) {
            val lambdaExpression = functionLiteral.parent
            if (lambdaExpression is KtLambdaExpression) {
                val lambdaArgument = lambdaExpression.parent
                if (lambdaArgument is KtLambdaArgument) {
                    val parent = lambdaArgument.parent
                    if (parent is KtCallExpression) {
                        return parent
                    }
                }
            }
        }
    }

    return null
}

/**
 * Get the containing Spek class, if the current scope is declared on the root.
 * <code>
 *     class MySpec: Spek({
 *          describe("something") { ... }
 *     })
 * </code>
 *
 * Current scope here is `describe` and we want to retrieve the KtClassOrObject element of `MySpec`.
 *
 */
private fun getRootScopeClassOrObject(callExpression: KtCallExpression): KtClassOrObject? {
    val block = callExpression.parent
    if (block is KtBlockExpression) {
        val functionLiteral = block.parent
        if (functionLiteral is KtFunctionLiteral) {
            val lambdaExpression = functionLiteral.parent
            if (lambdaExpression is KtLambdaExpression) {
                val valueArgument = lambdaExpression.parent
                if (valueArgument is KtValueArgument) {
                    val valueArgumentList = valueArgument.parent
                    if (valueArgumentList is KtValueArgumentList) {
                        val superTypeCallEntry = valueArgumentList.parent
                        if (superTypeCallEntry is KtSuperTypeCallEntry) {
                            val superTypeList = superTypeCallEntry.parent
                            if (superTypeList is KtSuperTypeList) {
                                val cls = superTypeList.parent
                                if (cls is KtClassOrObject) {
                                    return cls
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return null
}

private fun pathBuilderFromKtClassOrObject(element: KtLightClass?): PathBuilder? {
    if (element != null) {
        if (isSpekSubclass(element) && isSpekRunnable(element)) {
            val fqName = element.getKotlinFqName()
            if (fqName != null) {
                val pkg = fqName.parent().asString()
                val cls = fqName.shortName().asString()
                return PathBuilder()
                    .append(pkg)
                    .append(cls)
            }
        }
    }
    return null
}

fun isIdentifier(element: PsiElement): Boolean {
    val node = element.node
    if (node != null) {
        val elementType = node.elementType
        if (elementType is KtToken) {
            return elementType.toString() == "IDENTIFIER"
        }
    }
    return false
}
