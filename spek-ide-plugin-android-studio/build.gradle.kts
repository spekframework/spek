import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

val buildMatrix = mapOf(
    "AS35" to ij.BuildConfig(
        "191.8026.42",
        "Studio3.5",
        "AS34",
        ij.VersionRange("191.8026", "191.8026.*"),
        arrayOf("org.jetbrains.kotlin:1.3.30-release-Studio3.5-1")
    ),
    "AS36" to ij.BuildConfig(
        "192.7142.36",
        "Studio3.6",
        "AS34",
        ij.VersionRange("192.7142.36", "192.7142.36.*"),
        arrayOf("android", "java", "org.jetbrains.kotlin:1.3.61-release-Studio3.6-1")
    ),
    "AS40" to ij.BuildConfig(
        "193.6911.18",
        "Studio4.0",
        "AS34",
        ij.VersionRange("193.6911.18", "193.6911.18.*"),
        arrayOf("android", "java", "org.jetbrains.kotlin:1.3.70-release-Studio4.0-1")
    )
)

val sdkVersion = project.properties["as.version"] ?: "AS40"
val settings = checkNotNull(buildMatrix[sdkVersion])

intellij {
    pluginName = "Spek Framework"
    val plugins = arrayOf("gradle", "android") + settings.deps
    setPlugins(*plugins)
    version = settings.sdk
}

sourceSets {
    main {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDirs("src/${settings.extraSource}/kotlin")
        }
    }
}

dependencies {
    compile(project(":spek-ide-plugin-intellij-base-jvm"))
    compileOnly(kotlin("stdlib"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    buildPlugin {
        archiveBaseName.value("")
        archiveVersion.value("${project.version}-${settings.prefix}")
    }

    patchPluginXml {
        setVersion("${project.version}-${settings.prefix}")
        setSinceBuild(settings.version.since)
        setUntilBuild(settings.version.until)
    }
}

apply {
    plugin("publish-plugin")
}
