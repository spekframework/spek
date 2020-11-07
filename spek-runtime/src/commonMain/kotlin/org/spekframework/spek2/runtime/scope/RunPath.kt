package org.spekframework.spek2.runtime.scope

/**
 * Specifies which tests are run by Spek.
 */
class RunPath (val path: String) {
    init {
        if (!RUN_PATH_REGEX.matches(path)) {
            throw IllegalArgumentException("Invalid run path: '$path'")
        }
    }

    /**
     * Check if this run path is a parent of the given [path].
     */
    fun isParentOf(path: RunPath): Boolean {
        return path.path.startsWith(this.path)
    }

    class Builder(packageName: String, className: String) {
        private val scopes = mutableListOf<String>()

        init {
            scopes.addAll(packageName.split("."))
            scopes.add(className);
        }

        fun addScope(scope: String): Builder {
            scopes.add(scope)
            return this
        }

        fun build(): RunPath {
            val builder = StringBuilder()
            builder.append(SEPARATOR)

            return RunPath(
                StringBuilder()
                    .append(SEPARATOR)
                    .append(scopes.joinToString(SEPARATOR))
                    .toString()
            )
        }
    }

    companion object {
        private const val SEPARATOR = "/"
        private val RUN_PATH_REGEX = Regex("(^\\/\$)|(\\/[^\\/]+)+?")

        fun builder(packageName: String, className: String): Builder {
            return Builder(packageName, className)
        }

        fun builder(className: String) = builder("", className)
    }
}
