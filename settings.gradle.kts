enableFeaturePreview("STABLE_PUBLISHING")
enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "spek"

includeBuild("spek-gradle-plugin")

include("spek-dsl")
include("spek-kotlin-jvm-compiler-plugin")
include("spek-kotlin-native-compiler-plugin")
include("spek-runtime")
include("spek-runner:junit5")
include("integration-test")

val excludeIdePlugins: String? by settings

if (excludeIdePlugins != null) {
    include("spek-ide-plugin:interop-jvm")
    include("spek-ide-plugin:intellij-base")
    include("spek-ide-plugin:intellij-base-jvm")
    include("spek-ide-plugin:intellij-idea")
    include("spek-ide-plugin:android-studio")
}
