plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm {
        mavenPublication {
            artifactId = "spek-runtime-jvm"
        }

    }

    linuxX64("linux") {
        mavenPublication {
            artifactId = "spek-runtime-native-linux"
        }
    }

    macosX64("macOS") {
        mavenPublication {
            artifactId = "spek-runtime-native-macos"
        }
    }

    mingwX64("windows") {
        mavenPublication {
            artifactId = "spek-runtime-native-windows"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":spek-dsl"))
                implementation(kotlin("stdlib"))
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

        macosX64("macOS") {
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

val stubJavaDocJar by tasks.creating(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = arrayOf("metadata", "jvm")

publishing {
    kotlin.targets.forEach { target ->
        val targetPublication: Publication? = publications.findByName(target.name)
        if (targetPublication is MavenPublication) {
            targetPublication.artifact(stubJavaDocJar)
        }
    }
}
