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
import kotlin.time.measureTime

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

    suspend fun discoverAsync(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        val scopes = mutableListOf<GroupScopeImpl>()
        coroutineScope {
            filterScopes(discoveryRequest).collect { scope ->
                scopes.add(scope)
            }
        }

        return DiscoveryResult(scopes)
    }

    @OptIn(ExperimentalTime::class)
    fun discover(discoveryRequest: DiscoveryRequest): DiscoveryResult {
        lateinit var results: DiscoveryResult
        println("spek2: Discovery started.")
        val time = measureTime {
            doRunBlocking {
                if (isParallelDiscoveryEnabled(false)) {
                    println("spek2: Running discovery phase in parallel.")
                    withContext(Dispatchers.Default) {
                        results = discoverAsync(discoveryRequest)
                    }
                } else {
                    results = discoverAsync(discoveryRequest)
                }
            }
        }

        println("spek2: Discovery completed in ${time.inMilliseconds} ms")
        return results
    }

    // For internal use only!
    suspend fun executeAsync(request: ExecutionRequest) {
        Executor().execute(request)
    }

    // TODO: allow making the test run in parallel.
    fun execute(request: ExecutionRequest) {
        doRunBlocking {
            val job = coroutineScope {
                if (isParallelExecutionEnabled(false)) {
                    println("spek2: Running execution phase in parallel.")
                    launch(Dispatchers.Default) {
                        executeAsync(request)
                    }
                } else {
                    // inherit the dispatcher from doRunBlocking (which is an event loop backed by the main thread)
                    launch {
                        executeAsync(request)
                    }
                }
            }

            job.join()
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

    companion object {
        private const val DEFAULT_TIMEOUT = 0L
    }
}

expect fun isParallelDiscoveryEnabled(default: Boolean): Boolean
expect fun isParallelExecutionEnabled(default: Boolean): Boolean
expect fun getGlobalTimeoutSetting(default: Long): Long