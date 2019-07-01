import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

val buildMatrix = mapOf(
    "IJ182" to ij.BuildConfig(
        "2018.2.5",
        "IJ2018.2",
        "IJ182",
        ij.VersionRange("182.1", "182.*"),
        arrayOf("org.jetbrains.kotlin:1.3.30-release-IJ2018.2-1")
    ),
    "IJ183" to ij.BuildConfig(
        "183.4284.36",
        "IJ2018.3",
        "IJ183",
        ij.VersionRange("183.1", "183.*"),
        arrayOf("org.jetbrains.kotlin:1.3.30-release-IJ2018.3-1")
    ),
    "IJ191" to ij.BuildConfig(
        "2019.1",
        "IJ2019.1",
        "IJ183",
        ij.VersionRange("191.1", "191.*"),
        arrayOf("org.jetbrains.kotlin:1.3.31-release-IJ2019.1-1")
    ),
    "IJ192" to ij.BuildConfig(
        "192-EAP-SNAPSHOT",
        "IJ2019.2",
        "IJ183",
        ij.VersionRange("192.1", "192.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.40-release-IJ2019.2-1")
    )
)

val sdkVersion = project.properties["ij.version"] ?: "IJ192"
val settings = checkNotNull(buildMatrix[sdkVersion])

intellij {
    pluginName = "Spek Framework"
    setPlugins(*settings.deps)
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
