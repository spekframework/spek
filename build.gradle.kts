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
        jcenter()
    }

    if (Files.exists(Paths.get("$rootDir/local.properties"))) {
        val localProperties = java.util.Properties()
        localProperties.load(java.io.FileInputStream("$rootDir/local.properties"))
        localProperties.forEach { prop -> project.extra.set(prop.key as String, prop.value) }
    }

    var releaseMode = false
    if ("$version".matches(Regex("^\\d+\\.\\d+\\.\\d+(-rc\\.\\d+)?"))) {
        releaseMode = true
    }
    project.extra["releaseMode"] = releaseMode
}

listOf("check", "build", "clean").forEach { taskName ->
   task(taskName) {
       gradle.includedBuilds.forEach { includedBuild ->
           dependsOn(gradle.includedBuild(includedBuild.name).task(":$taskName"))
       }
    }
}
