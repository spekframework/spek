package org.spekframework.runtime.scope

data class Path(val name: String, val parent: Path?) {
    private val serialized by lazy {
        serialize(this)
    }

    fun resolve(name: String) = Path(name, this)

    fun asString(): String {
        return serialized
    }

    companion object {
        private val FAKE_ROOT = Path("fake-root", null)
        private val PATH_SEPARATOR = "||"

        fun from(path: String): Path {
            return path.split(PATH_SEPARATOR).fold(FAKE_ROOT) { initial, name ->
                val parent = if (initial === FAKE_ROOT) {
                    null
                } else {
                    initial
                }

                Path(name, parent)
            }
        }

        private fun serialize(path: Path): String {
            return if (path.parent == null) {
                path.name
            } else {
                "${serialize(path.parent)}$PATH_SEPARATOR${path.name}"
            }
        }
    }
}
