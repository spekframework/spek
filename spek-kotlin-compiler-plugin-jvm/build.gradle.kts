plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("compiler-embeddable"))

    implementation(Dependencies.autoService)
    kapt(Dependencies.autoService)
}

tasks {
    kapt {
        includeCompileClasspath = false
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
