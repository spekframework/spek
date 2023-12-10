plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
}

intellij {
    pluginName.set("spek-base")
    plugins.set(listOf("org.jetbrains.kotlin"))
    version.set("2023.3")
}

dependencies {
//    compileOnly(kotlin("stdlib"))
    implementation(project(":spek-runtime"))
//    {
//        exclude(group = "org.jetbrains.kotlin")
//    }
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

    patchPluginXml {
        sinceBuild.set("233.*")
    }
}
