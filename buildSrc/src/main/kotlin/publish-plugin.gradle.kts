val channel = if (project.extra["releaseMode"] == true) {
    "default"
} else {
    "dev"
}

val publishToken = propOrEnv("HUB_API_TOKEN")

//publishPlugin {
//    token = publishToken
//    channels = [channel]
//}