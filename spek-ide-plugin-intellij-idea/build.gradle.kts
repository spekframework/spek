import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

val buildMatrix = mapOf(
    "IJ193" to ij.BuildConfig(
        "193.6015.39",
        "IJ2019.3",
        "IJ183",
        ij.VersionRange("193.1", "193.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.50-release-IJ2019.2-1")
    ),
    "IJ201" to ij.BuildConfig(
        "201-EAP-SNAPSHOT",
        "IJ2020.1",
        "IJ183",
        ij.VersionRange("201.1", "201.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.61-release-IJ2019.3-1")
    ),
    "IJ202" to ij.BuildConfig(
        "202-EAP-SNAPSHOT",
        "IJ2020.2",
        "IJ183",
        ij.VersionRange("202.1", "202.*"),
        arrayOf("java", "org.jetbrains.kotlin:1.3.72-release-IJ2020.1-5")
    )
)

val sdkVersion = project.properties["ij.version"] ?: "IJ202"
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
