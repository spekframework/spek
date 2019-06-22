plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        
        jvm() {
            mavenPublication {
                artifactId = "spek-dsl-jvm"
            }

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

val stubJavaDocJar by tasks.creating(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = arrayOf("metadata", "jvm")

apply {
    plugin("publish")
}
