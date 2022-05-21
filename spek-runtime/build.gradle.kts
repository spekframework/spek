plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    metadata {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-metadata"
            pom {
                name.set("Spek Runtime Metadata")
                description.set("Kotlin metadata module for spek-runtime")
            }
        }
    }

    jvm {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-jvm"
            pom {
                name.set("Spek Runtime JVM")
                description.set("Kotlin JVM module for spek-runtime")
            }
        }

    }

    linuxX64("linux") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-native-linux"
            pom {
                name.set("Spek Runtime Native Linux")
                description.set("Kotlin Native linux module for spek-runtime")
            }
        }
    }

    macosX64("macos") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-native-macos"
            pom {
                name.set("Spek Runtime Native MacOS")
                description.set("Kotlin Native macos module for spek-runtime")
            }
        }
    }

    mingwX64("windows") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-native-windows"
            pom {
                name.set("Spek Runtime Native Windows")
                description.set("Kotlin Native windows module for spek-runtime")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":spek-dsl"))
                implementation(kotlin("stdlib-common"))
                implementation(Dependencies.kotlinCoroutinesCore)
            }
        }
        
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                    implementation(kotlin("reflect"))
                    implementation(Dependencies.classgraph)
                    implementation(Dependencies.kotlinCoroutinesCore)
                }
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        linuxX64("linux") {
            compilations["main"].defaultSourceSet {
                dependsOn(nativeMain)
            }
        }

        macosX64("macos") {
            compilations["main"].defaultSourceSet {
                dependsOn(nativeMain)
            }
        }

        mingwX64("windows") {
            compilations["main"].defaultSourceSet {
                dependsOn(nativeMain)
            }
        }
    }
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = when (currentOS) {
    OS.LINUX -> arrayOf("kotlinMultiplatform", "jvm", "js", "linux", "metadata")
    OS.WINDOWS -> arrayOf("windows")
    OS.MACOS -> arrayOf("macos")
}

apply {
    plugin("publish")
}

publishing {
    kotlin.targets.forEach { target ->
        val targetPublication: Publication? = publications.findByName(target.name)
        if (targetPublication is MavenPublication) {
            targetPublication.artifact(stubJavaDocJar.get())
        }
    }

    publications.findByName("kotlinMultiplatform")?.apply {
        if (this is MavenPublication) {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime"
            pom {
                name.set("Spek Runtime")
                description.set("Kotlin metadata module for spek-runtime")
            }
        }
    }
}
