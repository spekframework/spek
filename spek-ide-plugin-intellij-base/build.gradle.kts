plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

intellij {
    pluginName.set("spek-base")
    plugins.set(listOf("org.jetbrains.kotlin:1.3.61-release-IJ2018.3-1"))
    version.set("2018.3")
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
        sinceBuild.set("181.*")
    }
}
