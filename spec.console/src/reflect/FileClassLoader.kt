package org.spek.console.reflect;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author hadihariri
 */
public object FileClassLoader {
    public fun getClasses(folder : String, packageName : String ) : List<Class<*>> {
        val dirs = arrayListOf<File>();
        dirs.add(File(folder));
        val classes = arrayListOf<Class<*>>();
        dirs forEach { directory ->
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private fun findClasses(directory : File , packageName : String) : List<Class<*>> {
        val classes = arrayListOf<Class<*>>()
        if (!directory.exists()) return classes
        val files = directory.listFiles()
        if (files == null) return classes
        files forEach { file ->
            if (file.isDirectory())
                classes.addAll(findClasses(file, packageName + "." + file.getName()));

            if (file.getName().endsWith(".class"))
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
        }
        return classes;
    }
}
