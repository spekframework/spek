buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("${project.group}:spek-gradle-plugin")
    }
}

plugins {
    kotlin("multiplatform")
    id("org.spekframework.spek2.gradle-plugin")
}

kotlin {
    jvm()
    linuxX64("linux")
    macosX64("macos")
    mingwX64("windows")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":spek-dsl"))
                implementation(project(":spek-runtime"))
                implementation(kotlin("test"))
            }
        }

        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }

            compilations["test"].defaultSourceSet {
                dependencies {
                    runtimeOnly(kotlin("reflect"))
                    runtimeOnly(project(":spek-runner:junit5"))
                }
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val nativeTest by creating {
            dependsOn(commonTest)
        }

        linuxX64("linux") {
            compilations["main"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeMain)
                }
            }

            compilations["test"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeTest)
                }
            }
        }

        linuxX64("linux") {
            compilations["main"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeMain)
                }
            }

            compilations["test"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeTest)
                }
            }
        }

        macosX64("macos") {
            compilations["main"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeMain)
                }
            }

            compilations["test"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeTest)
                }
            }
        }

        mingwX64("windows") {
            compilations["main"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeMain)
                }
            }

            compilations["test"].defaultSourceSet {
                dependencies {
                    dependsOn(nativeTest)
                }
            }
        }
    }
}

tasks {
    val jvmTest by getting(Test::class) {
        useJUnitPlatform {
            includeEngines("spek2")
        }

        filter {
            includeTestsMatching("org.spekframework.spek2.*")
        }
    }
}

// This is required to substitute the versions of spek-runtime and the compiler plugins applied automatically by spek-gradle-plugin with the version provided by
// the spek-runtime and compiler plugin projects, rather than trying to download the not-yet-published version from a Maven repository.
configurations.forEach {
    it.resolutionStrategy.dependencySubstitution {
        substitute(module("$group:spek-runtime")).with(project(":spek-runtime"))
        substitute(module("$group:spek-kotlin-jvm-compiler-plugin")).with(project(":spek-kotlin-jvm-compiler-plugin"))
        substitute(module("$group:spek-kotlin-native-compiler-plugin")).with(project(":spek-kotlin-native-compiler-plugin"))
    }
}
