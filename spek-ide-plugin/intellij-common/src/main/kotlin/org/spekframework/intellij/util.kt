package org.spekframework.intellij

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.idea.refactoring.isAbstract
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.psi.*
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder

private val SPEK_CLASSES = listOf(
    "org.spekframework.spek2.Spek"
)

private val SYNONYM_CLASSES = listOf(
    "org.spekframework.spek2.meta.Synonym"
)

fun extractPath(element: PsiElement): Path? {
    return when (element) {
        is KtClassOrObject -> {
            pathBuilderFromKtClassOrObject(element)?.build()
        }
        is PsiDirectory -> {
            val pkg = element.getPackage()
            if (pkg != null) {
                PathBuilder()
                    .append(pkg.qualifiedName)
                    .build()
            } else {
                null
            }
        }
        is KtCallExpression -> {
             extractPath(element)
        }
        else -> null
    }
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

private fun extractPath(callExpression: KtCallExpression, buffer: List<String> = emptyList()): Path? {
    val calleeExpression = callExpression.calleeExpression
    if (calleeExpression != null) {
        val mainReference = calleeExpression.mainReference
        if (mainReference != null) {
            val resolved = mainReference.resolve()
            if (resolved != null && resolved is KtNamedFunction) {
                val synonym = extractSynonymAnnotation(resolved)
                if (synonym != null && !synonym.excluded) {
                    val description = extractDescription(callExpression)
                    if (description != null) {
                        val parentCallExpression = getParentCallExpression(callExpression)
                        val currentPath = "${synonym.prefix} $description".trim()
                        if (parentCallExpression != null) {
                            val newBuffer = mutableListOf<String>()
                            newBuffer.add(currentPath)
                            newBuffer.addAll(buffer)
                            return extractPath(parentCallExpression, newBuffer)
                        } else {
                            // probably root scope
                            val ktClassOrObject = getKtClassOrObject(callExpression)
                            if (ktClassOrObject != null) {
                                val builder = pathBuilderFromKtClassOrObject(ktClassOrObject)
                                // TODO: PathBuilder is immutable for some reason :noidea: why I did this :)
                                if (builder != null) {
                                    builder.append(currentPath)
                                    buffer.forEach {
                                        builder.append(it)
                                    }

                                    return builder.build()
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

private enum class SynonymType {
    Group,
    Action,
    Test
}

private data class SynonymData(val type: SynonymType,
                               val prefix: String,
                               val excluded: Boolean)

private fun extractSynonymAnnotation(function: KtNamedFunction): SynonymData? {
    val lightMethod = function.toLightMethods().firstOrNull()
    val annotation = lightMethod?.annotations?.find {
        SYNONYM_CLASSES.contains(it.qualifiedName)
    }

    return annotation?.let {
        val type = annotation.findDeclaredAttributeValue("type")!!
        val prefix = annotation.findDeclaredAttributeValue("prefix")
        val excluded = annotation.findDeclaredAttributeValue("excluded")

        val synonymText = type.text
        val synonymType = if (synonymText.endsWith("SynonymType.Group")) {
            SynonymType.Group
        } else if (synonymText.endsWith("SynonymType.Action")) {
            SynonymType.Action
        } else if (synonymText.endsWith("SynonymType.Test")) {
            SynonymType.Test
        } else {
            LOG.warn("Unsupported synonym: $synonymText.")
            null
        }

        synonymType?.let {
            SynonymData(
                it,
                prefix?.let { it.text.removeSurrounding("\"") } ?: "",
                excluded?.let { it.text.toBoolean() } ?: false
            )
        }
    }
}

private fun extractDescription(callExpression: KtCallExpression, index: Int = 0): String? {
    val argument = callExpression.valueArguments.getOrNull(0)
    val expression = argument?.getArgumentExpression()

    return when (expression) {
        is KtStringTemplateExpression -> {
            if (!expression.hasInterpolation()) {
                expression.entries.first().text
            } else {
                null
            }
        }
        else -> null
    }
}

private fun getParentCallExpression(callExpression: KtCallExpression): KtCallExpression? {
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

private fun getKtClassOrObject(callExpression: KtCallExpression): KtClassOrObject? {
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

private fun pathBuilderFromKtClassOrObject(element: KtClassOrObject): PathBuilder? {
    if (isSpekSubclass(element) && isSpekRunnable(element)) {
        val fqName = element.fqName
        if (fqName != null) {
            val pkg = fqName.parent().asString()
            val cls = fqName.shortName().asString()
            return PathBuilder()
                .append(pkg)
                .append(cls)
        }
    }
    return null
}
