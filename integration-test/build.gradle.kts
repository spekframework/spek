plugins {
  kotlin("multiplatform")
  id("org.spekframework.spek2.multiplatform")
}

repositories {
  jcenter()
}

kotlin {
  metadata {}
  jvm {}
  linuxX64("linux") {}
  macosX64("macos") {}
  mingwX64("windows") {}

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
      }
    }

    val commonTest by getting {
      dependencies {
        implementation("org.spekframework.spek2:spek-dsl-metadata")
        implementation("org.spekframework.spek2:spek-runtime-metadata")
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
          implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.3")
          implementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
          implementation("org.mockito:mockito-core:2.23.4")
          runtimeOnly("org.spekframework.spek2:spek-runner-junit5")
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
          implementation("org.spekframework.spek2:spek-dsl-linux")
          implementation("org.spekframework.spek2:spek-runtime-linux")
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
          implementation("org.spekframework.spek2:spek-dsl-macos")
          implementation("org.spekframework.spek2:spek-runtime-macos")
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
          implementation("org.spekframework.spek2:spek-dsl-windows")
          implementation("org.spekframework.spek2:spek-runtime-windows")
        }
      }
    }
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
