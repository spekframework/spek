package org.jetbrains.spek.console

public data class Options(
        public val paths: List<String>,
        public val packageName: String,
        public val format: String,
        public val filename: String,
        public val cssFile: String)
