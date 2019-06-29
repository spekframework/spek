plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

intellij {
    pluginName = "spek-base-jvm"
    setPlugins("org.jetbrains.kotlin:1.3.30-release-IJ2018.1-1")
    version = "2018.1"
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
