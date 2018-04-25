package org.spekframework.intellij

import com.intellij.lang.jvm.JvmModifier
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.compiled.ClsArrayInitializerMemberValueImpl
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.elements.KtLightAnnotationForSourceEntry
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.core.getPackage
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
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

private val DESCRIPTIONS_CLASSES = listOf(
    "org.spekframework.spek2.meta.Descriptions"
)

fun extractPath(element: PsiElement): Path? {
    return when (element) {
        is KtClassOrObject -> {
            pathBuilderFromKtClassOrObject(element.toLightClass())?.build()
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
fun isSpekSubclass(element: KtLightClass?): Boolean {
    if (element != null) {
        val superClass = element.superClass as KtLightClass?

        if (superClass != null) {
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

private fun extractPath(callExpression: KtCallExpression, buffer: List<String> = emptyList()): Path? {
    val calleeExpression = callExpression.calleeExpression
    if (calleeExpression != null) {
        val mainReference = calleeExpression.mainReference
        if (mainReference != null) {
            val resolved = mainReference.resolve()
            if (resolved != null && resolved is KtNamedFunction) {
                val synonymContext = extractSynonymAnnotation(resolved)
                if (synonymContext != null && !synonymContext.isExcluded()) {
                    try {
                        val description = synonymContext.constructDescription(callExpression)
                        val parentCallExpression = getParentCallExpression(callExpression)
                        if (parentCallExpression != null) {
                            val newBuffer = mutableListOf<String>()
                            newBuffer.add(description)
                            newBuffer.addAll(buffer)
                            return extractPath(parentCallExpression, newBuffer)
                        } else {
                            // probably root scope
                            val ktClassOrObject = getKtClassOrObject(callExpression)
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

enum class PsiSynonymType {
    GROUP,
    ACTION,
    TEST
}

data class PsiSynonym(val annotation: PsiAnnotation) {
    val type: PsiSynonymType by lazy {
        val type = annotation.findAttributeValue("type")!!.text

        when {
            type.endsWith("SynonymType.GROUP") -> PsiSynonymType.GROUP
            type.endsWith("SynonymType.ACTION") -> PsiSynonymType.ACTION
            type.endsWith("SynonymType.TEST") -> PsiSynonymType.TEST
            else -> throw IllegalArgumentException("Unsupported synonym: $type.")
        }
    }

    val prefix: String by lazy {
        annotation.findAttributeValue("prefix")!!.text.removeSurrounding("\"")
    }

    val excluded: Boolean by lazy {
        annotation.findAttributeValue("excluded")!!.text.toBoolean()
    }
}

enum class PsiDescriptionLocation {
    TYPE_PARAMETER,
    VALUE_PARAMETER
}

class PsiDescription(val annotation: PsiAnnotation) {
    val index: Int by lazy {
        annotation.findAttributeValue("index")!!.text.toInt()
    }

    val location: PsiDescriptionLocation by lazy {
        val text = annotation.findAttributeValue("location")!!.text
        when {
            text.endsWith("DescriptionLocation.TYPE_PARAMETER") -> PsiDescriptionLocation.TYPE_PARAMETER
            text.endsWith("DescriptionLocation.VALUE_PARAMETER") -> PsiDescriptionLocation.VALUE_PARAMETER
            else -> throw IllegalArgumentException("Unknown location type: $text")
        }
    }
}

class PsiDescriptions(val annotation: PsiAnnotation) {
    val sources: Array<PsiDescription> by lazy {
        val value = annotation.findAttributeValue("sources")!! as ClsArrayInitializerMemberValueImpl
        val sources = value.initializers.map { it as PsiAnnotation }
            .map(::PsiDescription)
        sources.toTypedArray()
    }
}

/**
 * Marker exception for unsupported operations, we don't propagate this exceptions
 * so that the IDE will not explode.
 */
class UnsupportedFeatureException(message: String): Throwable(message)

class SynonymContext(val synonym: PsiSynonym, val descriptions: PsiDescriptions) {
    fun isExcluded(): Boolean = synonym.excluded

    fun constructDescription(callExpression: KtCallExpression): String {
        return descriptions.sources.map {
            when (it.location) {
                PsiDescriptionLocation.TYPE_PARAMETER -> throw UnsupportedFeatureException("Type parameter description is currently unsupported.")
                PsiDescriptionLocation.VALUE_PARAMETER -> {
                    val argument = callExpression.valueArguments.getOrNull(it.index)
                    val expression = argument?.getArgumentExpression()

                    when (expression) {
                        is KtStringTemplateExpression -> {
                            if (!expression.hasInterpolation()) {
                                // might be empty at some point, especially when user is still typing
                                expression.entries.firstOrNull()?.text ?: ""
                            } else {
                                throw UnsupportedFeatureException("Descriptions with interpolation are currently unsupported.")
                            }
                        }
                        else -> throw IllegalArgumentException("Value argument description should be a string.")
                    }
                }
                else -> IllegalArgumentException("Invalid location: ${it.location}")
            }
        }.fold(synonym.prefix) { prev, current ->
            "$prev $current"
        }
    }
}

private fun extractSynonymAnnotation(function: KtNamedFunction): SynonymContext? {
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
