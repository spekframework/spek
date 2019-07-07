package org.spekframework.intellij.domain

import com.intellij.openapi.components.ProjectComponent
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.refactoring.isAbstract
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isObjectLiteral
import org.jetbrains.kotlin.resolve.lazy.data.KtClassInfoUtil
import org.spekframework.spek2.runtime.scope.Path
import org.spekframework.spek2.runtime.scope.PathBuilder
import java.util.concurrent.ConcurrentHashMap

class ScopeDescriptorCache: ProjectComponent {
    private val cache = ConcurrentHashMap<String, Pair<Long, ScopeDescriptor.Group>?>()
    private val index = ConcurrentHashMap<String, ScopeDescriptor?>()

    fun findDescriptor(path: Path): ScopeDescriptor? {
        return index.getOrDefault(path.serialize(), null)
    }

    fun fromCallExpression(callExpression: KtCallExpression): ScopeDescriptor? {
        val context = fetchSynonymContext(callExpression)
        if (context != null) {
            return PsiTreeUtil.getParentOfType(callExpression, KtClassOrObject::class.java)?.let {
                fromClassOrObject(it)?.findDescriptorForElement(callExpression)
            }
        }
        return null
    }

    fun fromClassOrObject(clz: KtClassOrObject): ScopeDescriptor.Group? {
        if (clz.isAbstract() || clz.isObjectLiteral() || !isSpekSubclass(clz)) {
            return null
        }
        val fqName = checkNotNull(clz.fqName).asString()

        var cached = cache.getOrDefault(fqName, null)

        if (cached == null || cached.first < clz.modificationStamp) {
            cached = clz.modificationStamp to buildDescriptor(clz)
            cache[fqName] = cached
        }

        return checkNotNull(cached).second
    }

    private fun buildDescriptor(clz: KtClassOrObject): ScopeDescriptor.Group {
        val info = KtClassInfoUtil.createClassOrObjectInfo(clz)
        val builder = PathBuilder()
        if (info.containingPackageFqName.asString().isNotBlank()) {
            builder.appendPackage(info.containingPackageFqName.asString())
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
        return ScopeDescriptor.Group(path, clz, false, true, children.toList()).apply {
            index[path.serialize()] = this
        }
    }

    private fun buildScopes(parent: Path, block: KtBlockExpression): List<ScopeDescriptor> {
        val callExpressions = PsiTreeUtil.getChildrenOfTypeAsList(block, KtCallExpression::class.java)
        return callExpressions.mapNotNull { callExpression ->
                val synonymContext = fetchSynonymContext(callExpression)
                when {
                    synonymContext != null -> callExpression to synonymContext
                    else -> null
                }
            }.mapNotNull { (callExpression, synonymContext) ->
                try {
                    val description = synonymContext.constructDescription(callExpression)

                    val path = PathBuilder(parent)
                        .append(description)
                        .build()

                    val descriptor = when (synonymContext.synonym.type) {
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

                            ScopeDescriptor.Group(
                                    path, callExpression, synonymContext.isExcluded(),
                                    synonymContext.isRunnable(), children.toList()
                            )
                        }
                        PsiSynonymType.TEST -> ScopeDescriptor.Test(
                                path, callExpression, synonymContext.isExcluded(), synonymContext.isRunnable()
                        )
                    }

                    index[path.serialize()] = descriptor

                    descriptor
                } catch (e: UnsupportedFeatureException) {
                    // avoid ide from throwing up
                    null
                }
            }
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
                superClass = when (ref) {
                    is KtPrimaryConstructor -> ref.getContainingClassOrObject()
                    is KtClass -> ref
                    else -> null
                }

                if (superClass != null) {
                    break
                }
            }
        }

        return superClass
    }

    companion object {
        private val SPEK_CLASSES = listOf(
            "org.spekframework.spek2.Spek"
        )
        private val SYNONYM_CLASSES = listOf(
            "org.spekframework.spek2.meta.Synonym"
        )
        private val DESCRIPTIONS_CLASSES = listOf(
            "org.spekframework.spek2.meta.Descriptions"
        )
    }
}
