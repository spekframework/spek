package org.spek.console.reflect;

import org.spek.api.Spek
import org.reflections.Reflections

/**
 * @author hadihariri
 */
public object FileClassLoader {
    public fun <T : Spek> getClasses(clazz : Class<T>, packageName : String ) : List<Class<out Spek>>{
        val reflections = Reflections(packageName);
        return reflections.getSubTypesOf(clazz)!!.toList()
    }
}
