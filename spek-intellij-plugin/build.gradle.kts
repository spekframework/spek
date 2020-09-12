import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

val plugins = mutableListOf(
    // IntelliJ IDEA
    ij.PluginDescriptor(
        "IJ2019.3",
        "193.6015.39",
        ij.VersionRange("193.1", "193.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.72-release-IJ2019.3-5")
    ),
    ij.PluginDescriptor(
        "IJ2020.1",
        "201.6487",
        ij.VersionRange("201.1", "201.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.72-release-IJ2020.1-1")
    ),
    ij.PluginDescriptor(
        "IJ2020.2",
        "202.6948.69",
        ij.VersionRange("202.1", "202.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.72-release-IJ2020.1-1")
    ),

    // Android Studio
    ij.PluginDescriptor(
        "AS4.0",
        "193.6911.18",
        ij.VersionRange("193.6911.18", "193.6911.18.*"),
        arrayOf("android", "java", "org.jetbrains.kotlin:1.3.72-release-Studio4.0-1")
    )
)

val buildMatrix = plugins.map { it.identifier to it }
    .toMap()

val sdkVersion = project.properties["plugin.version"] ?: "IJ2020.2"
val settings = checkNotNull(buildMatrix[sdkVersion])

intellij {
    pluginName = "Spek Framework"
    setPlugins(*settings.deps)
    version = settings.sdk
}

sourceSets {
    main {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDirs("src/${settings.identifier}/kotlin")
        }
        resources.srcDirs("src/${settings.identifier}/resources")
    }
}

dependencies {
    implementation(project(":spek-ide-plugin-interop-jvm"))
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    buildPlugin {
        archiveBaseName.value("")
        archiveVersion.value("${project.version}-${settings.identifier}")
    }

    patchPluginXml {
        setVersion("${project.version}-${settings.identifier}")
        setSinceBuild(settings.version.since)
        setUntilBuild(settings.version.until)
    }
}

apply {
    plugin("publish-plugin")
}
