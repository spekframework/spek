plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    metadata {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-metadata"
        }
    }

    jvm {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-jvm"
        }
    }

    js {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-js"
        }
    }

    linuxX64("linux") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-native-linux"
        }
    }

    macosX64("macos") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-native-macos"
        }
    }

    mingwX64("windows") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-native-windows"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
        }

        js {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-js"))
                }
            }
        }
    }
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = when (currentOS) {
    OS.LINUX -> arrayOf("metadata", "jvm", "js", "linux")
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
}
