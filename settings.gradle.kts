rootProject.name = "spek"

include("spek-dsl")
include("spek-runtime")
include("spek-runner-junit5")
include("spek-kotlin-compiler-plugin-jvm")
include("spek-kotlin-compiler-plugin-native")
include("spek-gradle-plugin")

val excludeIdePlugins: String? by settings

if (excludeIdePlugins == null) {
    include("spek-ide-plugin-interop-jvm")
    include("spek-ide-plugin-intellij-base")
    include("spek-ide-plugin-intellij-base-jvm")
    include("spek-ide-plugin-intellij-idea")
    include("spek-ide-plugin-android-studio")
    include("spek-intellij-plugin")
}