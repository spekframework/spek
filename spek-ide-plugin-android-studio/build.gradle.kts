plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

val buildMatrix = mapOf(
    "AS36" to ij.BuildConfig(
        "192.7142.36",
        "Studio3.6",
        "AS34",
        ij.VersionRange("192.7142.36", "192.7142.36.*"),
        listOf("android", "java", "org.jetbrains.kotlin:1.3.61-release-Studio3.6-1")
    ),
    "AS40" to ij.BuildConfig(
        "193.6911.18",
        "Studio4.0",
        "AS34",
        ij.VersionRange("193.6911.18", "193.6911.18.*"),
        listOf("android", "java", "org.jetbrains.kotlin:1.3.70-release-Studio4.0-1")
    ),
    "AS41" to ij.BuildConfig(
        "201.7223.91",
        "Studio4.1",
        "AS34",
        ij.VersionRange("201.8743.12", "201.8743.12.*"),
        listOf("android", "java", "org.jetbrains.kotlin:1.3.72-release-Studio4.1-5")
    ),
    "AS42" to ij.BuildConfig(
        "202.7660.26",
        "Studio4.2",
        "AS34",
        ij.VersionRange("202.7660.26", "202.7660.26.*"),
        listOf("android", "java", "org.jetbrains.kotlin:1.4.10-release-Studio4.2-1")
    )
)

val sdkVersion = project.properties["as.version"] ?: "AS42"
val settings = checkNotNull(buildMatrix[sdkVersion])

intellij {
    pluginName.set("Spek Framework")
    plugins.set(listOf("gradle", "android") + settings.deps)
    version.set(settings.sdk)
}

sourceSets {
    main {
        sourceSets {
            kotlin {
                srcDir("src/${settings.extraSource}/kotlin")
            }
        }
    }
}

dependencies {
    api(project(":spek-ide-plugin-intellij-base-jvm"))
//    compileOnly(kotlin("stdlib"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
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

    buildSearchableOptions {
        enabled = false
    }
}

apply {
    plugin("publish-plugin")
}
