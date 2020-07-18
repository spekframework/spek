plugins {
    id("com.jfrog.bintray")
    `maven-publish` apply false
}

val bintrayUser = propOrEnv("BINTRAY_USER")
val bintrayApiKey = propOrEnv("BINTRAY_API_KEY")

var bintrayRepo = "spek-dev"
var doPublish = true

if (project.extra["releaseMode"] == true) {
    bintrayRepo = "spek"
    doPublish = false
}

val artifacts = project.extra["artifacts"] as Array<String>
bintray {
    user = bintrayUser
    key = bintrayApiKey
    publish = doPublish
    with(pkg) {
        repo = bintrayRepo
        desc = "Test framework for Kotlin"
        name = "spek2"
        userOrg = "spekframework"
        setLicenses("BSD New")
        setLabels("kotlin", "testing")
        vcsUrl = "https://github.com/spekframework/spek.git"
        githubRepo = "spekframework/spek"
        with(version) {
            name = rootProject.version.toString()
        }
    }

    setPublications(*artifacts)
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                url.set("https://github.com/spekframework/spek")
                licenses {
                    license {
                        name.set("Modified BSD")
                        url.set("https://github.com/spekframework/spek/blob/2.x/LICENSE.TXT")
                        distribution.set("repo")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/spekframework/spek")
                    developerConnection.set("scm:git:github.com:spekframework/spek.git")
                    tag.set("2.x")
                    url.set("https://github.com/spekframework/spek")
                }

                developers {
                    developer {
                        id.set("spek")
                        name.set("Spek Team")
                        url.set("https://spekframework.org")
                        email.set("team@spekframwork.org")
                    }
                }
            }
        }
    }
}