includeBuild("../") {
  dependencySubstitution {
    substitute(module("org.spekframework.spek2:spek-gradle-plugin:0.1.0")).with(project(":spek-gradle-plugin"))
    substitute(module("org.spekframework.spek2:spek-dsl")).with(project(":spek-dsl"))
    substitute(module("org.spekframework.spek2:spek-dsl-metadata")).with(project(":spek-dsl"))
    substitute(module("org.spekframework.spek2:spek-dsl-jvm")).with(project(":spek-dsl"))
    substitute(module("org.spekframework.spek2:spek-dsl-linux")).with(project(":spek-dsl"))
    substitute(module("org.spekframework.spek2:spek-dsl-windows")).with(project(":spek-dsl"))
    substitute(module("org.spekframework.spek2:spek-dsl-macos")).with(project(":spek-dsl"))
    substitute(module("org.spekframework.spek2:spek-runtime")).with(project(":spek-runtime"))
    substitute(module("org.spekframework.spek2:spek-runtime-metadata")).with(project(":spek-runtime"))
    substitute(module("org.spekframework.spek2:spek-runtime-jvm")).with(project(":spek-runtime"))
    substitute(module("org.spekframework.spek2:spek-runtime-linux")).with(project(":spek-runtime"))
    substitute(module("org.spekframework.spek2:spek-runtime-windows")).with(project(":spek-runtime"))
    substitute(module("org.spekframework.spek2:spek-runtime-macos")).with(project(":spek-runtime"))
    substitute(module("org.spekframework.spek2:spek-runner-junit5")).with(project(":spek-runner-junit5"))
    substitute(module("org.spekframework.spek2:spek-kotlin-compiler-plugin-jvm")).with(project(":spek-kotlin-compiler-plugin-jvm"))
    substitute(module("org.spekframework.spek2:spek-kotlin-compiler-plugin-native")).with(project(":spek-kotlin-compiler-plugin-native"))
  }
}

pluginManagement {
  repositories {
    jcenter()
    maven ("https://dl.bintray.com/kotlin/kotlin-eap")
    maven ("https://kotlin.bintray.com/kotlinx")
  }
  resolutionStrategy.eachPlugin {
    if (requested.id.id == "org.spekframework.spek2.multiplatform") {
      useModule("org.spekframework.spek2:spek-gradle-plugin:0.1.0")
    }
  }
}
