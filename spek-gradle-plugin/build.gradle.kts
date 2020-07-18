plugins {
    `kotlin-dsl`
//    kotlin("kapt")
}

repositories {
    jcenter()
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
//    kapt(autoService)
}

tasks {
//    kapt {
//        includeCompileClasspath = false
//    }

    jar {
        manifest {
            attributes(mapOf("Implementation-Version" to version))
        }
    }
}
