plugins {
    id("com.jfrog.bintray")
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
