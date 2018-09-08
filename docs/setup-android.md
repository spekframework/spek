# Setting up for Android
For non-android modules in your project you can follow the [JVM](setup-jvm.md) instructions.

```groovy
// junit5 doesn't support android projects out of the box
apply plugin: "de.mannodermaus.android-junit5"

android {
    ...

    // Add Kotlin source directory to all source sets
    sourceSets.each {
        it.java.srcDirs += "src/$it.name/kotlin"
    }

    testOptions {
        junitPlatform {
            filters {
                engines {
                    include 'spek2'
                }
            }
            jacocoOptions {
                // here goes all jacoco config, for example
                html.enabled = true
                xml.enabled = false
                csv.enabled = false
                unitTests.all {
                    testLogging.events = ["passed", "skipped", "failed"]
                }
            }
        }
    }
}

dependencies {
    implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

    // spek
    testImplementation "org.spekframework.spek2:spek-dsl-jvm:$spek_version"
    testImplementation "org.spekframework.spek2:spek-runner-junit5:$spek_version"

    // spek requires kotlin-reflect, omit when already in classpath
    testImplementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
}
```
