plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm {
        mavenPublication {
            artifactId = "spek-dsl-jvm"
        }
    }

    linuxX64("linux") {
        mavenPublication {
            artifactId = "spek-dsl-native-linux"
        }
    }

    macosX64("macOS") {
        mavenPublication {
            artifactId = "spek-dsl-native-macos"
        }
    }

    mingwX64("windows") {
        mavenPublication {
            artifactId = "spek-dsl-native-windows"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
        }

        linuxX64("linux") {
            mavenPublication {
                artifactId = "spek-dsl-native-linux"
            }
        }

        macosX64("macOS") {
            mavenPublication {
                artifactId = "spek-dsl-native-macos"
            }
        }

        mingwX64("windows") {
            mavenPublication {
                artifactId = "spek-dsl-native-windows"
            }
        }
    }
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = arrayOf("metadata", "jvm")

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
