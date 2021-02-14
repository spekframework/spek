plugins {
    `maven-publish`
    signing
}

var bintrayRepo = "spek-dev"
var doPublish = true

var releaseMode = project.extra["releaseMode"] == true

if (project.extra["releaseMode"] == true) {
    bintrayRepo = "spek"
    doPublish = false
}

val artifacts = (project.extra["artifacts"] as Array<String>).toHashSet()

tasks.withType<AbstractPublishToMaven>()
    .configureEach {
        onlyIf {
            artifacts.contains(publication.name)
        }
    }

signing {
    val signingKey = propOrEnv("OSSRH_SIGNING_KEY")
    val signingPassword = propOrEnv("OSSRH_SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    publishing.publications.all {
        if (artifacts.contains(name)) {
            sign(this)
        }
    }
}

publishing {
    repositories {
        maven {
            val targetRepo = if (releaseMode) {
                "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            } else {
                "https://oss.sonatype.org/content/repositories/snapshots/"
            }
            setUrl(targetRepo)

            credentials {
                username = propOrEnv("OSSRH_USERNAME")
                password = propOrEnv("OSSRH_PASSWORD")
            }
        }
    }

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