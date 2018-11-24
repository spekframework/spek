package org.spekframework.intellij

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.spekframework.intellij.domain.ScopeDescriptorCache
import org.spekframework.intellij.util.maybeGetContext

class SpekRunLineMarkerContributor: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        if (element !is KtNameReferenceExpression) {
            return null
        }

        val descriptorCache = checkNotNull(
            element.project.getComponent(ScopeDescriptorCache::class.java)
        )
        val context = maybeGetContext(element)
        val descriptor = when (context) {
            is KtClassOrObject -> descriptorCache.fromClassOrObject(context)
            is KtCallExpression -> descriptorCache.fromCallExpression(context)
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
