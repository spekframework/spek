plugins {
    kotlin("multiplatform")
    id("org.spekframework.spek2.multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    metadata {}
    jvm {}
//    linuxX64("linux") {}
//    macosX64("macos") {}
//    mingwX64("windows") {}

    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation(kotlin("stdlib"))
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
//                    implementation(kotlin("stdlib-jdk8"))
                }
            }

            compilations["test"].defaultSourceSet {
                dependencies {
                    implementation(Dependencies.mockitoKotlin)
                    implementation(Dependencies.mockitoCore)
                    runtimeOnly(kotlin("reflect"))
                    runtimeOnly(project(":spek-runner-junit5"))
                }
            }
        }

//        val nativeMain by creating {
//            dependsOn(commonMain)
//        }
//
//        val nativeTest by creating {
//            dependsOn(commonTest)
//        }
//
//        linuxX64("linux") {
//            compilations["main"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeMain)
//                }
//            }
//
//            compilations["test"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeTest)
//                }
//            }
//        }
//
//        linuxX64("linux") {
//            compilations["main"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeMain)
//                }
//            }
//
//            compilations["test"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeTest)
//                }
//            }
//        }
//
//        macosX64("macos") {
//            compilations["main"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeMain)
//                }
//            }
//
//            compilations["test"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeTest)
//                }
//            }
//        }
//
//        mingwX64("windows") {
//            compilations["main"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeMain)
//                }
//            }
//
//            compilations["test"].defaultSourceSet {
//                dependencies {
//                    dependsOn(nativeTest)
//                }
//            }
//        }
    }
}

spek2 {
    tests {
        val jvmTest by getting {
            useJUnitPlatform()
        }
    }
}

tasks {
    afterEvaluate {
        val runSpekJvmTest by getting(Test::class) {
            systemProperty("spek2.discovery.parallel.enabled", "")
            systemProperty("spek2.execution.parallel.enabled", "")
            filter {
                includeTestsMatching("org.spekframework.spek2.*")
            }
        }

        // TODO: make sure plugin does this
        val allSpekTests by getting {}
        val check by getting {
            dependsOn(allSpekTests)
        }
    }
}

// This is required to substitute the versions of spek-runtime and the compiler plugins applied automatically by spek-gradle-plugin with the version provided by
// the spek-runtime and compiler plugin projects, rather than trying to download the not-yet-published version from a Maven repository.
configurations.forEach {
    it.resolutionStrategy.dependencySubstitution {
        substitute(module("org.spekframework.spek2:spek-runtime")).using(project(":spek-runtime"))
    }
}
