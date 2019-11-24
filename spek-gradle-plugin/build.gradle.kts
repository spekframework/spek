plugins {
    id("org.ajoberstar.reckon") version "0.8.0"
    `kotlin-dsl`
    kotlin("kapt") version "1.3.60"
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.60"))
    }
}

repositories {
    jcenter()
}

reckon {
    scopeFromProp()
    stageFromProp("alpha", "rc", "final")
}

gradlePlugin {
    plugins {
        create("spekPlugin") {
            id = "org.spekframework.spek2.multiplatform"
            implementationClass = "org.spekframework.spek2.gradle.entry.MultiplatformPlugin"
        }
    }
}

val autoService = "com.google.auto.service:auto-service:1.0-rc4"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("gradle-plugin-api"))

    implementation(autoService)
    kapt(autoService)
}

tasks {
    kapt {
        includeCompileClasspath = false
    }

    jar {
        manifest {
            attributes(mapOf("Implementation-Version" to version))
        }
    }
}
