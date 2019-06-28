package org.spekframework.intellij

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.util.Function
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.spekframework.intellij.domain.ScopeDescriptorCache
import org.spekframework.intellij.util.maybeGetContext

class SpekRunLineMarkerContributor: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        val descriptorCache = checkNotNull(
            element.project.getComponent(ScopeDescriptorCache::class.java)
        )

        if (element !is LeafPsiElement) {
            return null
        }

        if (element.elementType != KtTokens.IDENTIFIER) {
            return null
        }

        val parent = element.parent

        val descriptor = when (parent) {
            is KtClassOrObject -> descriptorCache.fromClassOrObject(parent)
            is KtNameReferenceExpression -> {
                val nameRefParent = parent.parent
                if (nameRefParent is KtCallExpression) {
                    descriptorCache.fromCallExpression(nameRefParent)
                } else {
                    null
                }
            }
            else -> null
        }

        return descriptor?.let {
            if (!it.excluded && it.runnable) {
                val path = it.path
                Info(
                    AllIcons.RunConfigurations.TestState.Run,
                    Function<PsiElement, String> { "[Spek] Run ${path.name}"},
                    *ExecutorAction.getActions(0)
                )
            } else {
                null
            }
        }
    }
}
