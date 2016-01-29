package org.jetbrains.spek.console

import org.jetbrains.spek.api.SkippedException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.TestSpekAction
import org.jetbrains.spek.api.ignored
import java.io.File
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.jar.JarFile


public fun getUrlsForPaths(paths: List<String>): List<URL> {
    val urls = arrayListOf<URL>()
    paths.forEach {
        val file = File(it)
        if (file.exists()) {
            val url = file.toURI().toURL()
            urls.add(url)
        }
    }
    return urls
}


public fun findClassesInClassPath(packageName: String): List<String> {
    val classLoader = Thread.currentThread().getContextClassLoader()
    val packageUrl = classLoader?.getResource(packageName.replace('.', '/'))
    return findClassesInUrls(listOf(packageUrl!!), packageName)
}

public fun findClassesInUrls(urls: List<URL>, packageName: String): List<String> {
    val names = arrayListOf<String>()
    for (url in urls) {
        if (url.toString().endsWith(".jar")) {
            var jarFilename = URLDecoder.decode(url.getFile()!!, "UTF-8")
            val jarFile = JarFile(jarFilename)
            var jarEntries = jarFile.entries()
            while (jarEntries.hasMoreElements())  {
                var entryName = jarEntries.nextElement().getName()
                if (entryName.startsWith(packageName) && entryName.length > packageName.length +5) {
                    entryName = entryName.substring(packageName.length,entryName.lastIndexOf('.'))
                    names.add(entryName)
                }
            }
        } else {
            val uri = URI(url.toString() + "/" + packageName.replace('.', '/'))
            val folder = File(uri.getPath()!!)
            val contents = folder.listFiles()
            contents?.forEach {
                var entry = it.getName()
                val classIndex = entry.lastIndexOf('.')
                if (classIndex > 0) {
                    entry = entry.substring(0, classIndex)
                    names.add(entry)
                }
            }
        }
    }
    return names
}

public fun findSpecs(paths: List<String>, packageName: String): MutableList<TestSpekAction> {
    val result = arrayListOf<TestSpekAction>()
    val urls = getUrlsForPaths(paths)
    val classloader = URLClassLoader.newInstance(urls.toTypedArray())!!
    urls.forEach {
        val classes = findClassesInUrls(urls, packageName)
        classes.forEach {
            val loadedClass = classloader.loadClass(packageName + "." + it)
            if (Spek::class.java.isAssignableFrom(loadedClass!!)) {
                @Suppress("UNCHECKED_CAST")
                result.add(ClassSpek(loadedClass as Class<Spek>))
            }
        }
    }
    return result
}


private fun AnnotatedElement.checkSkipped() {
    val skip = getAnnotation(ignored::class.java)
    if (skip != null) throw SkippedException(skip.why)
}

public data class ExtensionFunctionSpek(val method: Method) : TestSpekAction {

    val recordedActions by lazy {
        val builder = object : Spek() {}
        method.checkSkipped()
        method.invoke(null, builder)
        builder.listGiven()
    }

    override fun description(): String = method.toString()

    override fun listGiven() = recordedActions

    override fun run(action: () -> Unit) {
        action()
    }
}

public data class ClassSpek<T : Spek>(val specificationClass: Class<out T>) : TestSpekAction {
    override fun description(): String = specificationClass.getName()

    val recordedActions by lazy {
        specificationClass.checkSkipped()
        specificationClass.newInstance().listGiven()
    }

    override fun listGiven() = recordedActions

    override fun run(action: () -> Unit) {
        action()
    }
}
