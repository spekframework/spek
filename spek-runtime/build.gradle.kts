plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    metadata {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-metadata"
        }
    }

    jvm {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-jvm"
        }

    }

    linuxX64("linux") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-native-linux"
        }
    }

    macosX64("macos") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-native-macos"
        }
    }

    mingwX64("windows") {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runtime-native-windows"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":spek-dsl"))
                implementation(kotlin("stdlib-common"))
                implementation(Dependencies.kotlinCoroutinesCoreCommon)
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
            dependencies {
                implementation(Dependencies.kotlinCoroutinesNative)
            }
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
