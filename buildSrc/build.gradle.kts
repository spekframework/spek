plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(kotlin("gradle-plugin", version = "1.5.31"))
    api("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    api("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    api("org.jetbrains.intellij.plugins:gradle-intellij-plugin:1.4.0")
    api("com.github.jengelman.gradle.plugins:shadow:4.0.2")
}
