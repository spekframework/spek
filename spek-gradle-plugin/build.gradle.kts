plugins {
    id("org.ajoberstar.reckon") version "0.8.0"
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.30"
    kotlin("kapt") version "1.3.30"
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
            id = "${project.group}.gradle-plugin"
            implementationClass = "org.spekframework.spek2.gradleplugin.SpekPlugin"
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
