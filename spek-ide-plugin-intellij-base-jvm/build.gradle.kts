plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

intellij {
    pluginName.set("spek-base-jvm")
    plugins.set(listOf("org.jetbrains.kotlin:1.3.61-release-IJ2018.3-1"))
    version.set("2018.3")
}

dependencies {
    compile(project(":spek-ide-plugin-intellij-base"))
    compile(project(path = ":spek-ide-plugin-interop-jvm", configuration = "shadow"))
    compileOnly(kotlin("stdlib"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
