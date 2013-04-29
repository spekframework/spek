package org.spek.maven.plugin

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.project.MavenProject
import org.spek.console.cmd.Options
import java.net.URL
import java.io.File
import java.net.URLClassLoader
import org.apache.maven.plugins.annotations.ResolutionScope
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo as mojo
import org.apache.maven.plugins.annotations.Parameter as parameter
import org.spek.console.cmd.runSpecs

mojo(name = "spek", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.TEST)
public open class SpekMojo: AbstractMojo() {
    parameter(defaultValue = "", property = "packageName")
    private var packageName: String = ""

    parameter(defaultValue = "true", property = "text")
    private var text: Boolean = true

    parameter(defaultValue = "false", property = "html")
    private var html: Boolean = false

    parameter(property = "outputFile")
    private var outputFile: String? = null

    parameter(property = "cssFile")
    private var cssFile: String? = null

    parameter(defaultValue = "\${project}", required = true, readonly = true)
    private var project: MavenProject? = null

    public override fun execute(): Unit {
        runWithProjectClassPath {
            runSpecs(Options(packageName, text, html, outputFile ?: "", cssFile ?: ""))
        }
    }

    private fun runWithProjectClassPath(action: () -> Unit) {
        val classLoader = Thread.currentThread().getContextClassLoader()!!

        var elements = project?.getTestClasspathElements()!!

        val urls = Array<URL>(elements.size, { File(elements.get(it).toString()).toURI().toURL() })

        val newClassLoader = URLClassLoader.newInstance(urls, classLoader)!!

        Thread.currentThread().setContextClassLoader(newClassLoader)
        try {
            action()
        } finally {
            Thread.currentThread().setContextClassLoader(classLoader)
        }
    }
}
