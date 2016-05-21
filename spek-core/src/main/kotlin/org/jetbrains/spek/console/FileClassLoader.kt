package org.jetbrains.spek.console

import org.jetbrains.spek.api.Spek
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.jar.JarFile


fun getUrlsForPaths(paths: List<String>): List<URL> {
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

fun findClassesInUrls(urls: List<URL>, packageName: String): List<String> {
    val names = arrayListOf<String>()
    for (url in urls) {
        if (url.toString().endsWith(".jar")) {
            var jarFilename = URLDecoder.decode(url.getFile()!!, "UTF-8")
            val jarFile = JarFile(jarFilename)
            var jarEntries = jarFile.entries()
            while (jarEntries.hasMoreElements()) {
                var entryName = jarEntries.nextElement().getName()
                if (entryName.startsWith(packageName) && entryName.length > packageName.length + 5) {
                    entryName = entryName.substring(packageName.length, entryName.lastIndexOf('.'))
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

fun findSpecs(paths: List<String>, packageName: String): MutableList<Spek> {
    val result = arrayListOf<Spek>()
    val urls = getUrlsForPaths(paths)
    val classloader = URLClassLoader.newInstance(urls.toTypedArray())!!
    urls.forEach {
        val classes = findClassesInUrls(urls, packageName)
        classes.forEach {
            val loadedClass = classloader.loadClass(packageName + "." + it)
            if (Spek::class.java.isAssignableFrom(loadedClass!!)) {
                val spekInstance = loadedClass.newInstance() as Spek
                result.add(spekInstance)
            }
        }
    }
    return result
}
