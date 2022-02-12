import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.nio.file.Files
import java.nio.file.Paths

plugins {
    id("org.ajoberstar.reckon") version "0.9.0"
}

reckon {
    scopeFromProp()
    stageFromProp("alpha", "rc", "final")
}


allprojects {
    repositories {
        mavenCentral()
        maven ("https://www.jetbrains.com/intellij-repository/releases")
        maven ("https://www.jetbrains.com/intellij-repository/snapshots")
    }

    if (Files.exists(Paths.get("$rootDir/local.properties"))) {
        val localProperties = java.util.Properties()
        localProperties.load(java.io.FileInputStream("$rootDir/local.properties"))
        localProperties.forEach { prop -> project.extra.set(prop.key as String, prop.value) }
    }

    var releaseMode = false
    if ("$version".matches(Regex("^\\d+\\.\\d+\\.\\d+(-rc\\.\\d+)?"))) {
        releaseMode = true
    } else {
        version = "$version-SNAPSHOT"
    }
    project.extra["releaseMode"] = releaseMode

    afterEvaluate {
        tasks.withType<KotlinCompile<*>> {
            if (this is KotlinJvmCompile) {
                kotlinOptions {
                    apiVersion = "1.4"
                    jvmTarget = "11"
                }
            } else {
                kotlinOptions {
                    // KN requires 1.4
                    apiVersion = "1.4"
                }
            }
        }
    }
}

listOf("check", "build", "clean").forEach { taskName ->
   task(taskName) {
       gradle.includedBuilds.forEach { includedBuild ->
           dependsOn(gradle.includedBuild(includedBuild.name).task(":$taskName"))
       }
    }
}
