plugins {
  kotlin("multiplatform")
  id("org.spekframework.spek2.multiplatform")
}

repositories {
  jcenter()
  maven ("https://dl.bintray.com/kotlin/kotlin-eap")
  maven ("https://kotlin.bintray.com/kotlinx")
}

kotlin {
  jvm {}
  js {}
  linuxX64("linux") {}
  macosX64("macos") {}
  mingwX64("windows") {}

  sourceSets {
    val commonMain by getting

    val commonTest by getting {
      dependencies {
        implementation("org.spekframework.spek2:spek-dsl")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
        implementation("org.spekframework.spek2:spek-runtime")
        implementation(kotlin("test"))
      }
    }

    jvm {
      compilations["test"].defaultSourceSet {
        dependencies {
          runtimeOnly(kotlin("reflect"))
          implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
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
