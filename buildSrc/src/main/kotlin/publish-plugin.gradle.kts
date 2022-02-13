import org.jetbrains.intellij.tasks.PublishPluginTask

val channel = if (project.extra["releaseMode"] == true) {
    "default"
} else {
    "dev"
}

val publishToken = propOrEnv("HUB_API_TOKEN")

tasks {
    withType(PublishPluginTask::class) {
        token.set(publishToken)
        channels.set(listOf(channel))
    }
}