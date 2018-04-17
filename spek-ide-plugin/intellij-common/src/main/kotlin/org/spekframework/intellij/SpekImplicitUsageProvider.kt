package org.spekframework.intellij

import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.classes.KtLightClass

class SpekImplicitUsageProvider: ImplicitUsageProvider {
    override fun isImplicitWrite(element: PsiElement) = false

    override fun isImplicitRead(element: PsiElement) = false

    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (element is KtLightClass) {
            return isSpekSubclass(element) && isSpekRunnable(element)
        }
        return false
    }
}

