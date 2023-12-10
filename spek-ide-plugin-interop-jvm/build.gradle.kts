plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":spek-runtime")) {
        exclude(group = "org.jetbrains.kotlin")
    }
//    compileOnly(kotlin("stdlib"))
    implementation(Dependencies.kotlinArgParser)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    shadowJar {
        relocate("com.xenomachina", "shadow.com.xenomachina")

        dependencies {
            exclude(dependency("org.jetbrains.kotlin:.*:.*"))
        }
    }
}
