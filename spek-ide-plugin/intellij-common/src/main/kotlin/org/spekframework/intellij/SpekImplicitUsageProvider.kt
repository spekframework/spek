package org.spekframework.intellij

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.spekframework.intellij.domain.ScopeDescriptorCache

class SpekImplicitUsageProvider: ImplicitUsageProvider {
    override fun isImplicitWrite(element: PsiElement) = false

    override fun isImplicitRead(element: PsiElement) = false

    override fun isImplicitUsage(element: PsiElement): Boolean {
        val clz = when (element) {
            is KtClassOrObject -> element
            is KtLightClass -> element.kotlinOrigin
            else -> null
        }
        return clz?.let {
            ScopeDescriptorCache.fromClassOrObject(it) != null
        } ?: false
    }
}

