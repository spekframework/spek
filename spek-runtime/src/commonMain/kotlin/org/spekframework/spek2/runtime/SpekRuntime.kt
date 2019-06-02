package org.spekframework.spek2.runtime

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.runtime.execution.DiscoveryRequest
import org.spekframework.spek2.runtime.execution.DiscoveryResult
import org.spekframework.spek2.runtime.execution.ExecutionRequest
import org.spekframework.spek2.runtime.lifecycle.LifecycleManager
import org.spekframework.spek2.runtime.scope.*
import org.spekframework.spek2.runtime.util.ClassUtil

private const val DEFAULT_TIMEOUT = 10000L
class SpekRuntime {
    fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = discoveryRequest.context.getTests()
            .map { testInfo ->
                val matchingPath = discoveryRequest.paths.firstOrNull { it.intersects(testInfo.path) }
                testInfo to matchingPath
            }
            .filter { (_, matchingPath) -> matchingPath != null }
            .map { (testInfo, matchingPath) ->
                checkNotNull(matchingPath)
                val spec = resolveSpec(testInfo.createInstance(), testInfo.path)
                spec.filterBy(matchingPath)
                spec
            }
            .filter { spec -> !spec.isEmpty() }


        return DiscoveryResult(scopes)
    }

    fun execute(request: ExecutionRequest) = Executor().execute(request)

    private fun resolveSpec(instance: Spek, path: Path): GroupScopeImpl {
        val fixtures = FixturesAdapter()
        val lifecycleManager = LifecycleManager().apply {
            addListener(fixtures)
        }

        val (packageName, className) = ClassUtil.extractPackageAndClassNames(instance::class)

        val qualifiedName = if (packageName.isNotEmpty()) {
            "$packageName.$className"
        } else {
            className
        }
        val classScope = GroupScopeImpl(ScopeId(ScopeType.Class, qualifiedName), path, null, Skip.No, lifecycleManager, false)
        val collector = Collector(classScope, lifecycleManager, fixtures, CachingMode.TEST, DEFAULT_TIMEOUT)

        try {
            instance.root.invoke(collector)
        } catch (e: Exception) {
            collector.beforeGroup { throw e }
            classScope.addChild(TestScopeImpl(
                ScopeId(ScopeType.Scope, "Discovery failure"),
                path.resolve("Discovery failure"),
                classScope,
                DEFAULT_TIMEOUT,
                {},
                Skip.No,
                lifecycleManager
            ))
        }

        return classScope
    }
}
