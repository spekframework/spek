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
        listOf("java", "org.jetbrains.kotlin:1.3.50-release-IJ2019.2-1")
    ), "IJ201" to ij.BuildConfig(
        "201.8743.12",
        "IJ2020.1",
        "IJ183",
        ij.VersionRange("201.1", "201.*"),
        listOf("java", "org.jetbrains.kotlin:1.3.61-release-IJ2019.3-1")
    ), "IJ202" to ij.BuildConfig(
        "202.8194.7",
        "IJ2020.2",
        "IJ183",
        ij.VersionRange("202.1", "202.*"),
        listOf("java", "org.jetbrains.kotlin:1.3.72-release-IJ2020.1-5")
    ), "IJ203" to ij.BuildConfig(
        "203.5981.155",
        "IJ2020.3",
        "IJ183",
        ij.VersionRange("203.1", "203.*"),
        listOf("java", "org.jetbrains.kotlin:1.4.0-release-IJ2020.2-1")
    ), "IJ211" to ij.BuildConfig(
        "211.7628.21",
        "IJ2021.1",
        "IJ183",
        ij.VersionRange("211.1", "211.*"),
        listOf("java", "org.jetbrains.kotlin:211-1.4.21-release-IJ6556.4")
    ), "IJ212" to ij.BuildConfig(
        "212.4746.92",
        "IJ2021.2",
        "IJ183",
        ij.VersionRange("212.1", "212.*"),
        listOf("java", "org.jetbrains.kotlin:212-1.4.32-release-IJ2230")
    ),
    "IJ213" to ij.BuildConfig(
        "213.6461.79",
        "IJ2021.3",
        "IJ183",
        ij.VersionRange("213.1", "223.*"),
        listOf("java", "org.jetbrains.kotlin:213-1.6.10-release-944-IJ6461.79")
    ),
    "IJ233" to ij.BuildConfig(
        "233.11799.241",
        "IJ2023.3",
        "IJ183",
        ij.VersionRange("233.1", "233.*"),
        listOf("java", "org.jetbrains.kotlin"), // :231-1.7.10-release-334-IJ8228
        JavaVersion.VERSION_17
    )
)

val sdkVersion = project.properties["ij.version"] ?: "IJ233"
val settings = checkNotNull(buildMatrix[sdkVersion])

java {
    sourceCompatibility = settings.javaVersion
    targetCompatibility = settings.javaVersion
}

intellij {
    pluginName.set("Spek Framework")
    plugins.set(settings.deps)
    version.set(settings.sdk)
    updateSinceUntilBuild.set(true)
    sameSinceUntilBuild.set(false)
}

sourceSets {
    main {
        kotlin {
            srcDirs("src/${settings.extraSource}/kotlin")
        }
    }
}

dependencies {
    api(project(":spek-ide-plugin-intellij-base-jvm"))
}

tasks {
    compileKotlin {
//        version = settings.kotlinVersion
        kotlinOptions {
            jvmTarget = settings.javaVersion.toString()
        }
    }

    buildPlugin {
        archiveBaseName.value("")
        archiveVersion.value("${project.version}-${settings.prefix}")
    }

    patchPluginXml {
        setVersion("${project.version}-${settings.prefix}")
        sinceBuild.set(settings.version.since)
        untilBuild.set(settings.version.until)
    }
}

apply {
    plugin("publish-plugin")
}
