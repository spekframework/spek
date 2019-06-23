import org.jetbrains.intellij.tasks.PublishTask

val channel = if (project.extra["releaseMode"] == true) {
    "default"
} else {
    "dev"
}

val publishToken = propOrEnv("HUB_API_TOKEN")

tasks {
    withType(PublishTask::class) {
        setToken(publishToken)
        setChannels(channel)
    }
}
