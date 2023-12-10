plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(kotlin("gradle-plugin", version = "1.9.21"))
    api("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    api("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    api("org.jetbrains.intellij.plugins:gradle-intellij-plugin:1.16.+")
    api("org.apache.ant:ant:1.10.13") // needed for shadow jar zip
}
