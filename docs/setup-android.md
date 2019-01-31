# Setting up for Android
The main repository contains an [android sample](https://github.com/spekframework/spek/tree/2.x/samples/android) which
you can check out instead of this section.

## Build
### Android modules
To be able to run tests from gradle, we need to apply a third party [plugin](https://github.com/mannodermaus/android-junit5) as Gradle's
native support for JUnit 5 does not support android modules.

```groovy
// Required as JUnit 5 doesn't support android modules out of the box.
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
    implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"

    // spek
    testImplementation "org.spekframework.spek2:spek-dsl-jvm:$spek_version"
    testImplementation "org.spekframework.spek2:spek-runner-junit5:$spek_version"

    // spek requires kotlin-reflect, omit when already in classpath
    testImplementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
}
```

### Non-android modules
The setup assumes you are using Gradle `4.7` or above as it has a built-in support for JUnit 5.

```groovy
repositories {
    jcenter()
}

// setup dependencies
dependencies {
    // some version of Kotlin
    implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version'

    testImplementation 'org.spekframework.spek2:spek-dsl-jvm:$spek_version'
    testImplementation 'org.spekframework.spek2:spek-runner-junit5:$spek_version'
    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
}

// setup the test task
test {
    useJUnitPlatform {
        includeEngines 'spek2'
    }
}
```

## Running tests
See [Running tests](running.md).