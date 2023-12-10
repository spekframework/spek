plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

intellij {
    pluginName.set("spek-base-jvm")
    plugins.set(listOf("org.jetbrains.kotlin"))
    version.set("2023.3")
}

dependencies {
    api(project(":spek-ide-plugin-intellij-base"))
    api(project(path = ":spek-ide-plugin-interop-jvm", configuration = "shadow"))
//    compileOnly(kotlin("stdlib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}
