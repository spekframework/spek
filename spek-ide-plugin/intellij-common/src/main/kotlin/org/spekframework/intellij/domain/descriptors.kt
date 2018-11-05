package org.spekframework.intellij.domain

import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.refactoring.isAbstract
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.lazy.data.KtClassInfoUtil
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import java.util.concurrent.ConcurrentHashMap

sealed class ScopeDescriptor(val path: Path, val element: KtElement) {
    class Group(path: Path, element: KtElement, val children: List<ScopeDescriptor>): ScopeDescriptor(path, element) {
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
    class Test(path: Path, element: KtElement): ScopeDescriptor(path, element)
}

private val SPEK_CLASSES = listOf(
    "org.spekframework.spek2.Spek"
)

private val SYNONYM_CLASSES = listOf(
    "org.spekframework.spek2.meta.Synonym"
)

private val DESCRIPTIONS_CLASSES = listOf(
    "org.spekframework.spek2.meta.Descriptions"
)


object ScopeDescriptorCache {
    private val cache = ConcurrentHashMap<String, Pair<Long, ScopeDescriptor.Group>>()

    fun toDescriptor(callExpression: KtCallExpression): ScopeDescriptor? {
        val context = fetchSynonymContext(callExpression)
        if (context != null) {
            return PsiTreeUtil.getParentOfType(callExpression, KtClassOrObject::class.java)?.let {
                toDescriptor(it)?.findDescriptorForElement(callExpression)
            }
        }
        return null
    }

    fun toDescriptor(clz: KtClassOrObject): ScopeDescriptor.Group? {
        // TODO(rr): check @Ignore
        if (!isSpekSubclass(clz) || clz.isAbstract()) {
            return null
        }

        val cached = cache.compute(checkNotNull(clz.fqName).asString()) { _, value ->
            if (value != null && value.first == clz.modificationStamp) {
                value
            } else {
                clz.modificationStamp to buildDescriptor(clz)
            }
        }
        return checkNotNull(cached).second
    }

    private fun buildDescriptor(clz: KtClassOrObject): ScopeDescriptor.Group {
        val info = KtClassInfoUtil.createClassLikeInfo(clz)
        val builder = PathBuilder()
        if (info.containingPackageFqName.asString().isNotBlank()) {
            builder.append(info.containingPackageFqName.asString())
        }
        val path = builder
            .append(checkNotNull(clz.fqName).shortName().asString())
            .build()

        val superTypeCall = clz.superTypeListEntries.filterIsInstance<KtSuperTypeCallEntry>()
            .firstOrNull()

        val children = mutableListOf<ScopeDescriptor>()
        if (superTypeCall != null) {
            val lambda = superTypeCall.valueArguments.map { it.getArgumentExpression() }
                .filterIsInstance<KtLambdaExpression>()
                .firstOrNull()

            if (lambda != null) {
                lambda.bodyExpression?.let { body ->
                    children.addAll(buildScopes(path, body))
                }
            }
        }
        return ScopeDescriptor.Group(path, clz, children.toList())
    }

    private fun buildScopes(parent: Path, block: KtBlockExpression): List<ScopeDescriptor> {
        val callExpressions = PsiTreeUtil.getChildrenOfTypeAsList(block, KtCallExpression::class.java)
        val scopes = mutableListOf<ScopeDescriptor>()

        callExpressions.forEach { callExpression ->
            val synonymContext = fetchSynonymContext(callExpression)

            if (synonymContext != null) {
                val description = synonymContext.constructDescription(callExpression)

                val path = PathBuilder(parent)
                    .append(description)
                    .build()

                val descriptor = when(synonymContext.synonym.type) {
                    PsiSynonymType.GROUP -> {
                        val lambdaArgument = callExpression.lambdaArguments.firstOrNull()
                        val children = mutableListOf<ScopeDescriptor>()
                        if (lambdaArgument != null) {
                            val lambdaExpression = lambdaArgument.getLambdaExpression()
                            if (lambdaExpression != null) {
                                lambdaExpression.bodyExpression?.let { body ->
                                    children.addAll(buildScopes(path, body))
                                }
                            }
                        }

                        ScopeDescriptor.Group(path, callExpression, children.toList())
                    }
                    PsiSynonymType.TEST -> ScopeDescriptor.Test(path, callExpression)
                }

                scopes.add(descriptor)
            }
        }

        return scopes.toList()
    }

    private fun fetchSynonymContext(callExpression: KtCallExpression): SynonymContext? {
        return fetchNamedFunction(callExpression)?.let(::extractSynonymContext)
    }

    private fun fetchNamedFunction(callExpression: KtCallExpression): KtNamedFunction? {
        val mainReference = callExpression.calleeExpression?.mainReference
        val resolved = mainReference?.resolve()
        if (resolved is KtNamedFunction) {
            return resolved
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

    private fun isSpekSubclass(element: KtClassOrObject): Boolean {
        val superClass = getSuperClass(element)
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

                entry.lambdaArguments
                if (ref is KtPrimaryConstructor) {
                    superClass = ref.getContainingClassOrObject()
                    break
                }
            }
        }

        return superClass
    }
}