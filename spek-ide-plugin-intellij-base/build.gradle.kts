plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

intellij {
    pluginName = "spek-base"
    setPlugins("org.jetbrains.kotlin:1.3.30-release-IJ2018.1-1")
    version = "2018.1"
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compile(project(":spek-runtime")) {
        exclude(group = "org.jetbrains.kotlin")
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    patchPluginXml {
        setSinceBuild("181.*")
    }
}
