package org.spek.console.cmd

data class Options(
        val path: String,
        val packageName: String,
        val toText: Boolean,
        val toHtml: Boolean,
        val filename: String,
        val cssFile: String)
