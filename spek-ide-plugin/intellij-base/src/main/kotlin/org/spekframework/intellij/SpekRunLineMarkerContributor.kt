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
        val descriptorCache = checkNotNull(
            element.project.getComponent(ScopeDescriptorCache::class.java)
        )
        val descriptor = when (element) {
            is KtClassOrObject -> descriptorCache.fromClassOrObject(element)
            is KtCallExpression -> descriptorCache.fromCallExpression(element)
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
