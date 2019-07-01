plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm {
        mavenPublication {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-dsl-jvm"
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
    }
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

project.extra["artifacts"] = when (currentOS) {
    OS.LINUX -> arrayOf("metadata", "jvm", "linux")
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
