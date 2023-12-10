package ij

import org.gradle.api.JavaVersion

data class VersionRange(val since: String, val until: String)

data class BuildConfig(
    val sdk: String,
    val prefix: String,
    val extraSource: String,
    val version: VersionRange,
    val deps: List<String> = emptyList(),
    val javaVersion: JavaVersion = JavaVersion.VERSION_17
)
