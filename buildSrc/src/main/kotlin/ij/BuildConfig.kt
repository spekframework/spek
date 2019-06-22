package ij

data class VersionRange(val since: String, val until: String)

data class BuildConfig(
    val sdk: String,
    val prefix: String,
    val extraSource: String,
    val version: VersionRange,
    val deps: Array<String> = emptyArray()
)
