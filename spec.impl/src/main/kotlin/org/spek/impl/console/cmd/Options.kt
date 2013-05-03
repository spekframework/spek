package org.spek.impl.console.cmd

data class Options(
        val packageName: String,
        val toText: Boolean,
        val toHtml: Boolean,
        val filename: String,
        val cssFile: String)
