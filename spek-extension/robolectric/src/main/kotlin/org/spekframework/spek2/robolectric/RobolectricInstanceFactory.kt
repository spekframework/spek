package org.spekframework.spek2.robolectric

import org.robolectric.ConfigMerger
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.AndroidInterceptors
import org.robolectric.annotation.Config
import org.robolectric.internal.AndroidConfigurer
import org.robolectric.internal.BuckManifestFactory
import org.robolectric.internal.DefaultManifestFactory
import org.robolectric.internal.GradleManifestFactory
import org.robolectric.internal.ManifestFactory
import org.robolectric.internal.ManifestIdentifier
import org.robolectric.internal.MavenManifestFactory
import org.robolectric.internal.SandboxFactory
import org.robolectric.internal.SdkConfig
import org.robolectric.internal.SdkEnvironment
import org.robolectric.internal.bytecode.ClassHandler
import org.robolectric.internal.bytecode.InstrumentationConfiguration
import org.robolectric.internal.bytecode.Interceptors
import org.robolectric.internal.bytecode.SandboxConfig
import org.robolectric.internal.bytecode.ShadowMap
import org.robolectric.internal.bytecode.ShadowWrangler
import org.robolectric.internal.dependency.CachedDependencyResolver
import org.robolectric.internal.dependency.DependencyResolver
import org.robolectric.internal.dependency.LocalDependencyResolver
import org.robolectric.internal.dependency.PropertiesDependencyResolver
import org.robolectric.manifest.AndroidManifest
import org.robolectric.res.Fs
import org.robolectric.util.Logger
import org.robolectric.util.ReflectionHelpers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.InstanceFactory
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.util.ArrayList
import java.util.HashMap
import java.util.Properties
import kotlin.reflect.KClass

object RobolectricInstanceFactory: InstanceFactory {
    private val appManifestsCache = HashMap<ManifestIdentifier, AndroidManifest>()

    @Transient private var dependencyResolver: DependencyResolver? = null

    lateinit private var interceptors: Interceptors

    lateinit var testClass: Class<*>

    init {
        SecureRandom()
    }

    override fun <T : Spek> create(spek: KClass<T>): T {
        testClass = spek.java
        val config = getConfig(testClass)
        val androidManifest = getAppManifest(config)
        interceptors = Interceptors(findInterceptors())
        val sdkEnvironment = getSandbox(config, androidManifest)

        // Configure shadows *BEFORE* setting the ClassLoader. This is necessary because
        // creating the ShadowMap loads all ShadowProviders via ServiceLoader and this is
        // not available once we install the Robolectric class loader.
        configureShadows(sdkEnvironment)

        val bootstrappedTestClass = sdkEnvironment.bootstrappedClass<Any>(testClass)
        try {
            val t = bootstrappedTestClass
                .getConstructor(SdkEnvironment::class.java, Config::class.java, AndroidManifest::class.java)
                .newInstance(sdkEnvironment, config, androidManifest)
            return t as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun getBuildSystemApiProperties(): Properties? {
        val resourceAsStream = javaClass.getResourceAsStream("/com/android/tools/test_config.properties") ?: return null

        try {
            val properties = Properties()
            properties.load(resourceAsStream)
            return properties
        } catch (e: IOException) {
            return null
        }

    }

    private fun findInterceptors() = AndroidInterceptors.all()

    private fun getInterceptors() = interceptors

    private fun configureShadows(sdkEnvironment: SdkEnvironment) {
        val builder = createShadowMap().newBuilder()

        // Configure shadows *BEFORE* setting the ClassLoader. This is necessary because
        // creating the ShadowMap loads all ShadowProvider s via ServiceLoader and this is
        // not available once we install the Robolectric class loader.


        val shadowMap = builder.build()
        sdkEnvironment.replaceShadowMap(shadowMap)

        sdkEnvironment.configure(createClassHandler(shadowMap, sdkEnvironment), getInterceptors())
    }

    private fun createShadowMap() = ShadowMap.EMPTY

    private fun createClassHandler(shadowMap: ShadowMap, sdkEnvironment: SdkEnvironment): ClassHandler {
        return ShadowWrangler(shadowMap, sdkEnvironment.sdkConfig.apiLevel, interceptors)
    }

    private fun getExtraShadows(): Array<Class<*>> {
        val shadowClasses = ArrayList<Class<*>>()
        addShadows(shadowClasses, testClass.getAnnotation(SandboxConfig::class.java))
        return shadowClasses.toTypedArray()
    }

    private fun addShadows(shadowClasses: MutableList<Class<*>>, annotation: SandboxConfig?) {
        annotation?.shadows?.forEach {
            shadowClasses.add(it.java.javaClass)
        }
    }

    private fun getSandbox(config: Config, androidManifest: AndroidManifest): SdkEnvironment {
        return SandboxFactory.INSTANCE.getSdkEnvironment(
            createClassLoaderConfig(config),
            getJarResolver(),
            SdkConfig(pickSdkVersion(config, androidManifest)))
    }

    private fun getAppManifest(config: Config): AndroidManifest {
        val manifestFactory = getManifestFactory(config)
        val identifier = manifestFactory.identify(config)

        synchronized(appManifestsCache) {
            return appManifestsCache.computeIfAbsent(identifier, { manifestFactory.create(identifier) })
        }
    }

    /**
     * Detects which build system is in use and returns the appropriate ManifestFactory implementation.
     *
     * Custom TestRunner subclasses may wish to override this method to provide alternate configuration.
     *
     * @param config Specification of the SDK version, manifest file, package name, etc.
     */
    private fun getManifestFactory(config: Config): ManifestFactory {
        val buildSystemApiProperties = getBuildSystemApiProperties()
        if (buildSystemApiProperties != null) {
            return DefaultManifestFactory(buildSystemApiProperties)
        }

        val buildConstants = config.constants

        return if (BuckManifestFactory.isBuck()) {
            BuckManifestFactory()
        } else if (buildConstants.java != Void::class.java) {
            GradleManifestFactory()
        } else {
            MavenManifestFactory()
        }
    }

    private fun pickSdkVersion(config: Config?, manifest: AndroidManifest?): Int {
        return if (config != null && config.sdk.size > 1) {
            throw IllegalArgumentException("Spek does not support multiple values for @Config.sdk")
        } else if (config != null && config.sdk.size == 1) {
            config.sdk[0]
        } else manifest?.targetSdkVersion ?: SdkConfig.FALLBACK_SDK_VERSION
    }

    /**
     * NOTE: originally in RobolectricTestRunner getConfig takes Method as parameter
     * and is a bit more complicated
     */
    private fun getConfig(clazz: Class<*>): Config {
        // This will always be non empty since every class has basic methods like toString.
        val method = clazz.methods[0]
        val configMerger = ConfigMerger()
        return configMerger.getConfig(clazz, method, Config.Builder.defaults().build())
    }

    private fun getJarResolver(): DependencyResolver {
        if (dependencyResolver == null) {
            if (java.lang.Boolean.getBoolean("robolectric.offline")) {
                val dependencyDir = System.getProperty("robolectric.dependency.dir", ".")
                dependencyResolver = LocalDependencyResolver(File(dependencyDir))
            } else {
                val cacheDir = File(File(System.getProperty("java.io.tmpdir")), "robolectric")

                val mavenDependencyResolverClass = ReflectionHelpers.loadClass(RobolectricTestRunner::class.java.classLoader,
                    "org.robolectric.internal.dependency.MavenDependencyResolver")
                val dependencyResolver = ReflectionHelpers.callConstructor(mavenDependencyResolverClass) as DependencyResolver
                if (cacheDir.exists() || cacheDir.mkdir()) {
                    Logger.info("Dependency cache location: %s", cacheDir.absolutePath)
                    this.dependencyResolver = CachedDependencyResolver(dependencyResolver, cacheDir, (60 * 60 * 24 * 1000).toLong())
                } else {
                    this.dependencyResolver = dependencyResolver
                }
            }

            val buildPathPropertiesUrl = javaClass.classLoader.getResource("robolectric-deps.properties")
            if (buildPathPropertiesUrl != null) {
                Logger.info("Using Robolectric classes from %s", buildPathPropertiesUrl.path)

                val propertiesFile = Fs.fileFromPath(buildPathPropertiesUrl.file)
                try {
                    dependencyResolver = PropertiesDependencyResolver(propertiesFile, dependencyResolver)
                } catch (e: IOException) {
                    throw RuntimeException("couldn't read " + buildPathPropertiesUrl, e)
                }

            }
        }

        return dependencyResolver!!
    }

    /**
     * Create an [InstrumentationConfiguration] suitable for the provided [FrameworkMethod].
     *
     * Custom TestRunner subclasses may wish to override this method to provide alternate configuration.
     *
     * @return an [InstrumentationConfiguration]
     */
    private fun createClassLoaderConfig(config: Config): InstrumentationConfiguration {
        val builder = InstrumentationConfiguration.newBuilder()
            .doNotAcquirePackage("java.")
            .doNotAcquirePackage("sun.")
            .doNotAcquirePackage("org.robolectric.annotation.")
            .doNotAcquirePackage("org.robolectric.internal.")
            .doNotAcquirePackage("org.robolectric.util.")
            .doNotAcquirePackage("org.spekframework.spek2.")

        for (shadowClass in getExtraShadows()) {
            val shadowInfo = ShadowMap.getShadowInfo(shadowClass)
            builder.addInstrumentedClass(shadowInfo.shadowedClassName)
        }

        addInstrumentedPackages(builder)
        AndroidConfigurer.configure(builder, getInterceptors())
        AndroidConfigurer.withConfig(builder, config)
        return builder.build()
    }

    private fun addInstrumentedPackages(builder: InstrumentationConfiguration.Builder) {
        val classConfig = testClass.getAnnotation(SandboxConfig::class.java)
        if (classConfig != null) {
            for (pkgName in classConfig.instrumentedPackages) {
                builder.addInstrumentedPackage(pkgName)
            }
        }
    }

}
