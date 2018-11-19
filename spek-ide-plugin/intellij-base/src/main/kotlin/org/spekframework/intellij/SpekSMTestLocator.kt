package org.spekframework.intellij

import com.intellij.execution.Location
import com.intellij.execution.PsiLocation
import com.intellij.execution.testframework.sm.runner.SMTestLocator
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.spekframework.intellij.domain.ScopeDescriptorCache
import org.spekframework.spek2.runtime.scope.PathBuilder

object SpekSMTestLocator: SMTestLocator {
    override fun getLocation(protocol: String, path: String, project: Project, scope: GlobalSearchScope): List<Location<PsiElement>> {
        if (protocol != "spek") {
            throw AssertionError("Unsupported protocol: $protocol.")
        }
        val descriptorCache = checkNotNull(
            project.getComponent(ScopeDescriptorCache::class.java)
        )
        val descriptor = descriptorCache.findDescriptor(
            PathBuilder.parse(path).build()
        )
        return if (descriptor != null) {
            listOf<Location<PsiElement>>(PsiLocation(descriptor.element.navigationElement))
        } else {
            emptyList()
        }
    }
}