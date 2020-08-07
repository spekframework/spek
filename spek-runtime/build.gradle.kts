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

//    js {
//        mavenPublication {
//            groupId = "org.spekframework.spek2"
//            artifactId = "spek-runtime-js"
//            pom {
//                name.set("Spek Runtime JS")
//                description.set("Kotlin JS module for spek-runtime")
//            }
//        }
//    }

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
                implementation(Dependencies.kotlinCoroutinesCore)
            }
        }
        
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("reflect"))
                    implementation(Dependencies.classgraph)
                }
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val linuxMain by getting {
            dependsOn(nativeMain)
        }

        val macosMain by getting {
            dependsOn(nativeMain)
        }

        val windowsMain by getting {
            dependsOn(nativeMain)
        }
    }
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = when (currentOS) {
    OS.LINUX -> arrayOf("kotlinMultiplatform", "metadata", "jvm" ,/* "js",*/ "linux")
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

    publications {
        getByName("kotlinMultiplatform", MavenPublication::class) {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime"
        }
    }
}
