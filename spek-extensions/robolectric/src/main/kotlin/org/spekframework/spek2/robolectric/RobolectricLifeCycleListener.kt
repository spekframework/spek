package org.spekframework.spek2.robolectric

import android.os.Build
import org.robolectric.DefaultTestLifecycle
import org.robolectric.RobolectricTestRunner
import org.robolectric.TestLifecycle
import org.robolectric.android.internal.ParallelUniverse
import org.robolectric.annotation.Config
import org.robolectric.internal.ParallelUniverseInterface
import org.robolectric.internal.SdkEnvironment
import org.robolectric.internal.bytecode.Sandbox
import org.robolectric.internal.dependency.CachedDependencyResolver
import org.robolectric.internal.dependency.DependencyResolver
import org.robolectric.internal.dependency.LocalDependencyResolver
import org.robolectric.internal.dependency.PropertiesDependencyResolver
import org.robolectric.manifest.AndroidManifest
import org.robolectric.res.*
import org.robolectric.util.Logger
import org.robolectric.util.ReflectionHelpers
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.TestScope
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.*

class RobolectricLifeCycleListener(
    val sdkEnvironment: SdkEnvironment,
    val config:Config,
    val androidManifest: AndroidManifest): LifecycleListener {
    private val appResourceTableCache = HashMap<AndroidManifest, PackageResourceTable>()
    private var compiletimeSdkResourceTable: PackageResourceTable? = null
    @Transient private var dependencyResolver: DependencyResolver? = null

    var parallelUniverseInterface: ParallelUniverseInterface? = null
    var testLifecycle: TestLifecycle<*>? = null
    var priorContextClassLoader:ClassLoader? = null
    var orig: Thread? = null

    override fun beforeExecuteTest(test: TestScope) {
        super.beforeExecuteTest(test)
        priorContextClassLoader = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = sdkEnvironment.robolectricClassLoader

        parallelUniverseInterface = getHooksInterface(sdkEnvironment)
        val cl = sdkEnvironment.bootstrappedClass<TestLifecycle<*>>(getTestLifecycleClass())
        testLifecycle = ReflectionHelpers.newInstance<TestLifecycle<*>>(cl)

        parallelUniverseInterface!!.setSdkConfig(sdkEnvironment.sdkConfig)
        parallelUniverseInterface!!.resetStaticState(config)

        val sdkConfig = sdkEnvironment.sdkConfig
        val androidBuildVersionClass = sdkEnvironment.bootstrappedClass<Any>(Build.VERSION::class.java)
        ReflectionHelpers.setStaticField(androidBuildVersionClass, "SDK_INT", sdkConfig.apiLevel)
        ReflectionHelpers.setStaticField(androidBuildVersionClass, "RELEASE", sdkConfig.androidVersion)
        ReflectionHelpers.setStaticField(androidBuildVersionClass, "CODENAME", sdkConfig.androidCodeName)

        val systemResourceTable = sdkEnvironment.getSystemResourceTable(getJarResolver())
        val appResourceTable = getAppResourceTable(androidManifest)

        // This will always be non empty since every class has basic methods like toString.
        val randomMethod = getJarResolver().javaClass.methods[0]
        parallelUniverseInterface!!.setUpApplicationState(
            randomMethod,
            testLifecycle,
            androidManifest,
            config,
            RoutingResourceTable(getCompiletimeSdkResourceTable(), appResourceTable),
            RoutingResourceTable(systemResourceTable, appResourceTable),
            RoutingResourceTable(systemResourceTable))
        testLifecycle!!.beforeTest(null)
        orig = parallelUniverseInterface!!.mainThread
        parallelUniverseInterface!!.mainThread = Thread.currentThread()
    }

    /**
     * Returns the ResourceProvider for the compile time SDK.
     */
    private fun getCompiletimeSdkResourceTable(): PackageResourceTable {
        if (compiletimeSdkResourceTable == null) {
            val resourceTableFactory = ResourceTableFactory()
            compiletimeSdkResourceTable = resourceTableFactory.newFrameworkResourceTable(
                ResourcePath(android.R::class.java, null, null))
        }
        return compiletimeSdkResourceTable!!
    }

    protected fun getJarResolver(): DependencyResolver {
        if (dependencyResolver == null) {
            if (java.lang.Boolean.getBoolean("robolectric.offline")) {
                val dependencyDir = System.getProperty("robolectric.dependency.dir", ".")
                dependencyResolver = LocalDependencyResolver(File(dependencyDir))
            } else {
                val cacheDir = File(File(System.getProperty("java.io.tmpdir")), "robolectric")

                val mavenDependencyResolverClass = ReflectionHelpers.loadClass(
                    RobolectricTestRunner::class.java.classLoader,
                    "org.robolectric.internal.dependency.MavenDependencyResolver")
                val dependencyResolver = ReflectionHelpers.callConstructor(mavenDependencyResolverClass) as DependencyResolver
                if (cacheDir.exists() || cacheDir.mkdir()) {
                    Logger.info("Dependency cache location: %s", cacheDir.absolutePath)
                    this.dependencyResolver = CachedDependencyResolver(dependencyResolver, cacheDir,
                        (60 * 60 * 24 * 1000).toLong())
                } else {
                    this.dependencyResolver = dependencyResolver
                }
            }

            val buildPathPropertiesUrl = javaClass.getClassLoader().getResource("robolectric-deps.properties")
            if (buildPathPropertiesUrl != null) {
                Logger.info("Using Robolectric classes from %s", buildPathPropertiesUrl.getPath())

                val propertiesFile = Fs.fileFromPath(buildPathPropertiesUrl.getFile())
                try {
                    dependencyResolver = PropertiesDependencyResolver(propertiesFile, dependencyResolver)
                } catch (e: IOException) {
                    throw RuntimeException("couldn't read " + buildPathPropertiesUrl, e)
                }

            }
        }

        return dependencyResolver!!
    }

    private fun getAppResourceTable(appManifest: AndroidManifest): PackageResourceTable {
        var resourceTable: PackageResourceTable? = appResourceTableCache[appManifest]
        if (resourceTable == null) {
            resourceTable = ResourceMerger().buildResourceTable(appManifest)

            appResourceTableCache.put(appManifest, resourceTable)
        }
        return resourceTable
    }

    internal fun getHooksInterface(sandbox: Sandbox): ParallelUniverseInterface {
        val robolectricClassLoader = sandbox.robolectricClassLoader
        try {
            val clazz = robolectricClassLoader.loadClass(ParallelUniverse::class.java.name)
            val typedClazz = clazz.asSubclass(ParallelUniverseInterface::class.java)
            val constructor = typedClazz.getConstructor()
            return constructor.newInstance()
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        }

    }

    override fun afterExecuteTest(test: TestScope) {
        try {
            parallelUniverseInterface!!.tearDownApplication()
        } finally {
            try {
                //                internalAfterTest(method, bootstrappedMethod);
            } finally {
                parallelUniverseInterface!!.resetStaticState(
                    config) // afterward too, so stuff doesn't hold on to classes?
            }
        }
        parallelUniverseInterface!!.setMainThread(orig);
        Thread.currentThread().contextClassLoader = priorContextClassLoader
    }

    /**
     * An instance of the returned class will be created for each test invocation.
     *
     * Custom TestRunner subclasses may wish to override this method to provide alternate configuration.
     *
     * @return a class which implements [TestLifecycle]. This implementation returns a [DefaultTestLifecycle].
     */
    protected fun getTestLifecycleClass(): Class<out TestLifecycle<*>> {
        return DefaultTestLifecycle::class.java
    }
}
