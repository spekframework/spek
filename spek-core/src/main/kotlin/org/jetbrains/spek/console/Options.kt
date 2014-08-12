package org.jetbrains.spek.console

data class Options(
        val paths: List<String>,
        val packageName: String,
        val format: String,
        val filename: String,
        val cssFile: String)
