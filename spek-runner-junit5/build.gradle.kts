plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
//    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":spek-runtime"))
    implementation(Dependencies.junitPlatformEngine)

    testImplementation(Dependencies.hamkrest)
    testImplementation(Dependencies.junitJupiterApi)
    testImplementation(Dependencies.mockitoKotlin)
    testImplementation(Dependencies.junitPlatformLauncher)

    testRuntimeOnly(Dependencies.mockitoInline)
    testRuntimeOnly(Dependencies.junitJupiterEngine)
}

val archive = "spek-runner-junit5"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    test {
        useJUnitPlatform {
            excludeEngines("spek2")
        }
    }

    jar {
        archiveBaseName.value(archive)
    }
}

configurations["apiElements"].attributes {
    attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
}

configurations["runtimeElements"].attributes {
    attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
}

val sourceJar by tasks.registering(Jar::class) {
    archiveBaseName.value(archive)
    archiveClassifier.value("sources")
    from(sourceSets.main.get().java)
}

val stubJavaDocJar by tasks.registering(Jar::class) {
    archiveClassifier.value("javadoc")
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            groupId = "org.spekframework.spek2"
            artifactId = "spek-runner-junit5"
            from(components["java"])
            artifact(sourceJar.get())
            artifact(stubJavaDocJar.get())

            pom {
                name.set("Spek Runner Junit 5")
                description.set("Spek runner for JUnit 5")
            }
        }
    }
}

project.extra["artifacts"] = arrayOf("maven")

apply {
    plugin("publish")
}
