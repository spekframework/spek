plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compile(project(":spek-runtime")) {
        exclude(group = "org.jetbrains.kotlin")
    }
    compileOnly(kotlin("stdlib"))
    compile(Dependencies.kotlinArgParser)
}

tasks {
    shadowJar {
        relocate("com.xenomachina", "shadow.com.xenomachina")

        dependencies {
            exclude(dependency("org.jetbrains.kotlin:.*:.*"))
        }
    }
}
