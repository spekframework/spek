import org.gradle.internal.os.OperatingSystem

enum class OS {
    LINUX,
    MACOS,
    WINDOWS
}

val currentOS: OS by lazy {
    val current = OperatingSystem.current()
    when {
        current.isLinux -> OS.LINUX
        current.isMacOsX -> OS.MACOS
        current.isWindows -> OS.WINDOWS
        else -> throw AssertionError("Unsupported os: ${current.name}")
    }
}
