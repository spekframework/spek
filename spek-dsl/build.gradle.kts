plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    metadata {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-metadata"
            pom {
                name.set("Spek DSL Metadata")
                description.set("Kotlin metadata module for spek-dsl")
            }
        }
    }

    jvm {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-jvm"
            pom {
                name.set("Spek DSL JVM")
                description.set("Kotlin JVM module for spek-dsl")
            }
        }
    }

    js {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-js"
            pom {
                name.set("Spek DSL JS")
                description.set("Kotlin JS module for spek-dsl")
            }
        }
    }

    linuxX64("linux") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-native-linux"
            pom {
                name.set("Spek DSL Native Linux")
                description.set("Kotlin Native linux module for spek-dsl")
            }
        }
    }

    macosX64("macos") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-native-macos"
            pom {
                name.set("Spek DSL Native MacOS")
                description.set("Kotlin Native macos module for spek-dsl")
            }
        }
    }

    mingwX64("windows") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-native-windows"
            pom {
                name.set("Spek DSL Native Windows")
                description.set("Kotlin Native windows module for spek-dsl")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation(kotlin("stdlib-common"))
            }
        }

        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
//                    implementation(kotlin("stdlib-jdk8"))
                }
            }
        }

        js {
            compilations["main"].defaultSourceSet {
                dependencies {
//                    implementation(kotlin("stdlib-js"))
                }
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
            artifactId = "spek-dsl"
            pom {
                name.set("Spek DSL")
                description.set("Kotlin metadata module for spek-dsl")
            }
        }
    }
}
