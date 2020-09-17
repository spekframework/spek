package org.spekframework.spek2.runtime

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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

class SpekRuntime {
    private suspend fun CoroutineScope.filterScopes(discoveryRequest: DiscoveryRequest): Flow<GroupScopeImpl> = flow {
        val tasks = mutableListOf<Deferred<GroupScopeImpl?>>()
        discoveryRequest.context.getTests().forEach { testInfo ->
            val matchingPath = discoveryRequest.paths.firstOrNull { it.intersects(testInfo.path) }
            if (matchingPath != null) {
                val task = async {
                    val spec = resolveSpec(testInfo.createInstance(), testInfo.path)
                    spec.filterBy(matchingPath)
                    if (!spec.isEmpty()) {
                        spec
                    } else {
                        null
                    }

                }
                tasks.add(task)
            }
        }

        tasks.forEach { task ->
            task.await()?.let { emit(it) }
        }
    }

    @UseExperimental(ExperimentalTime::class)
    fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = mutableListOf<GroupScopeImpl>()
        val time = measureTime {
            doRunBlocking {
                if (isConcurrentDiscoveryEnabled(false)) {
                    withContext(Dispatchers.Default) {
                        filterScopes(discoveryRequest).collect { scope ->
                            scopes.add(scope)
                        }
                    }
                } else {
                    filterScopes(discoveryRequest).collect { scope ->
                        scopes.add(scope)
                    }
                }
            }
        }

        if (isDebuggingEnabled()) {
            println("Spek discovery completed in $time ms")
        }
        return DiscoveryResult(scopes)
    }

    fun execute(request: ExecutionRequest) {
        doRunBlocking {
            if (isConcurrentExecutionEnabled(false)) {
                withContext(Dispatchers.Default) {
                    Executor().execute(request)
                }
            } else {
                Executor().execute(request)
            }
        }
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

    fun isDebuggingEnabled(): Boolean {
        return isDebuggingEnabled(false)
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 0L
    }
}

expect fun isConcurrentDiscoveryEnabled(default: Boolean): Boolean
expect fun isConcurrentExecutionEnabled(default: Boolean): Boolean
expect fun getGlobalTimeoutSetting(default: Long): Long
expect fun isDebuggingEnabled(default: Boolean): Boolean

expect fun measureTime(block: () -> Unit): Long