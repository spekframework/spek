enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "spek"

includeBuild("./spek-gradle-plugin") {
    dependencySubstitution {
        substitute(module("org.spekframework.spek2:spek-gradle-plugin:0.1.0")).with(project(":"))
    }
}

pluginManagement {
    resolutionStrategy.eachPlugin {
        if (requested.id.id == "org.spekframework.spek2.multiplatform") {
            useModule("org.spekframework.spek2:spek-gradle-plugin:0.1.0")
        }
    }
}

include("spek-dsl")
include("spek-runtime")
include("spek-runner-junit5")
//include("spek-kotlin-compiler-plugin-jvm")
//include("spek-kotlin-compiler-plugin-native")
include("integration-test")

val excludeIdePlugins: String? by settings

if (excludeIdePlugins == null) {
    include("spek-ide-plugin-interop-jvm")
    include("spek-ide-plugin-intellij-base")
    include("spek-ide-plugin-intellij-base-jvm")
    include("spek-ide-plugin-intellij-idea")
    include("spek-ide-plugin-android-studio")
}