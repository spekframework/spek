repositories {
  jcenter()
  maven ("https://dl.bintray.com/kotlin/kotlin-eap")
  maven ("https://kotlin.bintray.com/kotlinx")
}

dependencies {
  implementation(kotlin("gradle-plugin", version = "1.4.0-rc"))
}