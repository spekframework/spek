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
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class SpekRuntime {
    private fun filterScopes(discoveryRequest: DiscoveryRequest, concurrency: Int): List<GroupScopeImpl> {
        val results = mutableListOf<GroupScopeImpl>()
        val handles = mutableListOf<TaskHandle>()
        val runner = TaskRunner(concurrency)
        discoveryRequest.context.getTests().forEach { testInfo ->
            val matchingPath = discoveryRequest.paths.firstOrNull { it.intersects(testInfo.path) }
            if (matchingPath != null) {
                val task = runner.runTask {
                    val spec = resolveSpec(testInfo.createInstance(), testInfo.path)
                    spec.filterBy(matchingPath)
                    if (!spec.isEmpty()) {
                        results.add(spec)
                    }
                }
                handles.add(task)
            }
        }

        // wait
        handles.forEach { handle ->
            try {
                handle.await()
            } catch (e: Throwable) {
                println("An error has occurred in discovery: ${e.message}")
            }
        }

        return results
    }

    @UseExperimental(ExperimentalTime::class)
    fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = mutableListOf<GroupScopeImpl>()
        val time = measureTime {
            val concurrency = if (isParallelDiscoveryEnabled(false)) {
                getExecutionParallelism()
            } else {
                1
            }
            scopes.addAll(filterScopes(discoveryRequest, concurrency))
        }

        if (isDebuggingEnabled(false)) {
            println("Spek discovery completed in $time ms")
        }
        return DiscoveryResult(scopes)
    }

    fun execute(request: ExecutionRequest) {
        val concurrency = if (isParallelExecutionEnabled(false)) {
            if (isDebuggingEnabled(false)) {
                println("spek2: Running execution phase in parallel.")
            }
            getExecutionParallelism()
        } else {
            1
        }
        Executor().execute(request, concurrency)
    }

    private fun resolveSpec(instance: Spek, path: Path): GroupScopeImpl {
        val lifecycleManager = LifecycleManager()

        val (packageName, className) = ClassUtil.extractPackageAndClassNames(instance::class)

        val qualifiedName = if (packageName.isNotEmpty()) {
            "$packageName.$className"
        } else {
            className
        }
        val classScope = GroupScopeImpl(ScopeId(ScopeType.Class, qualifiedName), path, null, Skip.No, lifecycleManager, false)
        val collector = Collector(classScope, lifecycleManager, CachingMode.TEST, getGlobalTimeoutSetting(DEFAULT_TIMEOUT))

        try {
            instance.root.invoke(collector)
            collector.finalize()
        } catch (e: Exception) {
            collector.beforeGroup { throw e }
            classScope.addChild(TestScopeImpl(
                ScopeId(ScopeType.Scope, "Discovery failure"),
                path.resolve("Discovery failure"),
                classScope,
                getGlobalTimeoutSetting(DEFAULT_TIMEOUT),
                {},
                Skip.No,
                lifecycleManager
            ))
        }

        return classScope
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 0L
    }
}

expect fun isParallelDiscoveryEnabled(default: Boolean): Boolean
expect fun isParallelExecutionEnabled(default: Boolean): Boolean
expect fun getGlobalTimeoutSetting(default: Long): Long
expect fun getExecutionParallelism(): Int
expect fun isDebuggingEnabled(default: Boolean): Boolean